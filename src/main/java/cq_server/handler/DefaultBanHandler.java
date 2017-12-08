package cq_server.handler;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;

import cq_server.model.Player;

public class DefaultBanHandler implements IBanHandler {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultBanHandler.class);

	private final Map<Player, INonBlockingConnection> connections;

	private final List<String> bannedIps;

	public DefaultBanHandler(final Map<Player, INonBlockingConnection> connections, final List<String> bannedIps) {
		this.connections = connections;
		this.bannedIps = bannedIps;
	}

	@Override
	public void ban(final Player player) {
		try {
			final INonBlockingConnection connection = this.connections.get(player);
			if (connection != null && connection.isOpen()) {
				connection.close();
				LOG.info("ban for player: {}", player);
				final String remoteIP = connection.getRemoteAddress().getHostAddress();
				this.bannedIps.add(remoteIP);
			}
		} catch (BufferOverflowException | IOException e) {
			LOG.error(e.getMessage());
		}
	}
}
