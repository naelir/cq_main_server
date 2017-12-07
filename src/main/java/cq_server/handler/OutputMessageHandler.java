package cq_server.handler;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;

import cq_server.factory.IMessageFactory;
import cq_server.game.BasePlayer;

public class OutputMessageHandler implements IOutputMessageHandler {
	private static final Logger LOG = LoggerFactory.getLogger(OutputMessageHandler.class);

	private final Map<BasePlayer, INonBlockingConnection> connections;

	private final List<String> bannedIps;

	private final IMessageFactory messageFactory;

	public OutputMessageHandler(
			final Map<BasePlayer, INonBlockingConnection> connections,
			final List<String> bannedIps,
			final IMessageFactory messageFactory) {
		this.connections = connections;
		this.bannedIps = bannedIps;
		this.messageFactory = messageFactory;
	}

	@Override
	public boolean ban(final BasePlayer player) {
		try {
			final INonBlockingConnection connection = this.connections.get(player);
			if (connection != null && connection.isOpen()) {
				connection.close();
				LOG.info("ban for player: {}", player); 
				final String remoteIP = connection.getRemoteAddress().getHostAddress();
				return this.bannedIps.add(remoteIP);
			}
		} catch (BufferOverflowException | IOException e) {
			LOG.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean sendMessage(final BasePlayer player, final List<Object> data) {
		try {
			final INonBlockingConnection connection = this.connections.get(player);
			final String message = this.messageFactory.createOutputNetworkMessage(data);
			if (connection != null && connection.isOpen()) {
				connection.write(message);
				return true;
			}
		} catch (BufferOverflowException | IOException e) {
			LOG.error(e.getMessage());
		}
		return false;
	}
}
