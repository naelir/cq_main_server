package cq_server.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.INonBlockingConnection;

import cq_server.factory.IPlayerFactory;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.Player.Type;

public class OnConnectHandler implements IConnectHandler {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultInEventHandler.class);

	private final List<String> bannedIps;

	private final Map<Player, INonBlockingConnection> connections;

	private final Map<INonBlockingConnection, Player> connectedPlayers;

	private final IPlayerFactory playerFactory;

	private final Map<Player, Queue<OutEvent>> toSendMessages;

	public OnConnectHandler(final Builder builder) {
		this.bannedIps = builder.bannedIps;
		this.connections = builder.connections;
		this.connectedPlayers = builder.connectedPlayers;
		this.playerFactory = builder.playerFactory;
		this.toSendMessages = builder.toSendMessages;
	}

	@Override
	public boolean onConnect(final INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
		final String hostAddress = connection.getRemoteAddress().getHostAddress();
		if (this.bannedIps.contains(hostAddress))
			connection.close();
		else {
			final Player player = this.playerFactory.createPlayer(Type.ONLINE, hostAddress, null);
			this.connections.put(player, connection);
			this.connectedPlayers.put(connection, player);
			this.toSendMessages.put(player, new ConcurrentLinkedQueue<>());
			LOG.info("onConnect from ip: " + hostAddress);
		}
		return true;
	}

	public static final class Builder {
		private List<String> bannedIps;

		private Map<Player, INonBlockingConnection> connections;

		private Map<INonBlockingConnection, Player> connectedPlayers;

		private IPlayerFactory playerFactory;

		private Map<Player, Queue<OutEvent>> toSendMessages;

		public OnConnectHandler build() {
			return new OnConnectHandler(this);
		}

		public Builder setBannedIps(final List<String> bannedIps) {
			this.bannedIps = bannedIps;
			return this;
		}

		public Builder setConnectedPlayers(
				final Map<INonBlockingConnection, Player> connectedPlayers) {
			this.connectedPlayers = connectedPlayers;
			return this;
		}

		public Builder setConnections(final Map<Player, INonBlockingConnection> connections) {
			this.connections = connections;
			return this;
		}

		public Builder setPlayerFactory(final IPlayerFactory playerFactory) {
			this.playerFactory = playerFactory;
			return this;
		}

		public Builder setToSendMessages(final Map<Player, Queue<OutEvent>> toSendMessages) {
			this.toSendMessages = toSendMessages;
			return this;
		}
	}
}
