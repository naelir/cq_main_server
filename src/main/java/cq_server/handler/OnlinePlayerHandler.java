package cq_server.handler;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;

import cq_server.event.CmdChannel;
import cq_server.event.ListenChannel;
import cq_server.factory.IResponseFactory;
import cq_server.model.BaseChannel;
import cq_server.model.ChannelEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public class OnlinePlayerHandler implements IOutEventHandler {
	private static final Logger LOG = LoggerFactory.getLogger(OnlinePlayerHandler.class);

	private final Map<Player, INonBlockingConnection> connections;

	private final IResponseFactory responseFactory;

	private final Map<Player, CmdChannel> cmdChannels;

	private final Map<Player, ListenChannel> listenChannels;

	private final Map<Player, Queue<OutEvent>> toSendEvents;

	public OnlinePlayerHandler(final Map<Player, INonBlockingConnection> connections,
			final IResponseFactory responseFactory, final Map<Player, Queue<OutEvent>> toSendEvents) {
		this.connections = connections;
		this.responseFactory = responseFactory;
		this.cmdChannels = new ConcurrentHashMap<>();
		this.listenChannels = new ConcurrentHashMap<>();
		this.toSendEvents = toSendEvents;
	}

	@Override
	public void onChannelEvent(final ChannelEvent event) {
		final BaseChannel channel = event.getChannel();
		final Player player = event.getPlayer();
		if (channel instanceof ListenChannel) {
			final ListenChannel listenChannel = (ListenChannel) channel;
			this.listenChannels.put(player, listenChannel);
			final Queue<OutEvent> queue = this.toSendEvents.get(player);
			if (!queue.isEmpty()) {
				final OutEvent outEvent = queue.poll();
				this.onOutEvent(outEvent);
			}
		} else {
			final CmdChannel cmdChannel = (CmdChannel) channel;
			this.cmdChannels.put(player, cmdChannel);
		}
	}

	@Override
	public void onOutEvent(final OutEvent event) {
		final Player player = event.getPlayer();
		switch (event.getKind()) {
		case LISTEN:
			player.setListen(false);
			final ListenChannel listenChannel = this.listenChannels.remove(player);
			if (listenChannel != null) {
				listenChannel.setConnId(player.getId());
				final List<Object> list = new ArrayList<>();
				list.add(listenChannel);
				list.addAll(event.getMessages());
				this.send(player, list);
			} else {
				final Queue<OutEvent> queue = this.toSendEvents.get(player);
				queue.offer(event);
			}
			break;
		case CMD: {
			final CmdChannel cmdChannel = this.cmdChannels.remove(player);
			if (cmdChannel != null) {
				cmdChannel.setConnId(player.getId());
				final List<Object> list = new ArrayList<>();
				list.add(cmdChannel);
				list.addAll(event.getMessages());
				this.send(player, list);
			} else
				LOG.error("missing cmd channel");
			break;
		}
		default:
			break;
		}
	}

	private void send(final Player player, final List<Object> messages) {
		final INonBlockingConnection connection = this.connections.get(player);
		final String message = this.responseFactory.getResponse(messages);
		try {
			if (connection.isOpen())
				connection.write(message);
		} catch (BufferOverflowException | IOException e) {
			LOG.error("{}", e);
		}
	}
}
