package cq_server.handler;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;

import cq_server.command.ICommand;
import cq_server.event.CmdChannel;
import cq_server.event.DisconnectEvent;
import cq_server.factory.ICommandFactory;
import cq_server.model.BaseEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.Request;

public class OnDisconnectHandler implements IDisconnectHandler {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultInEventHandler.class);

	private final Map<Player, INonBlockingConnection> connections;

	private final Map<INonBlockingConnection, Player> connectedPlayers;

	private final ICommandFactory commandFactory;

	private final Map<Player, Queue<OutEvent>> toSendMessages;

	public OnDisconnectHandler(final Builder builder) {
		this.connections = builder.connections;
		this.connectedPlayers = builder.connectedPlayers;
		this.commandFactory = builder.commandFactory;
		this.toSendMessages = builder.toSendMessages;
	}

	@Override
	public boolean onDisconnect(final INonBlockingConnection connection) throws IOException {
		final String hostAddress = connection.getRemoteAddress()
			.getHostAddress();
		final Player player = this.connectedPlayers.get(connection);
		this.connections.remove(player);
		this.connectedPlayers.remove(connection);
		this.toSendMessages.remove(player);
		LOG.warn("onDisconnect player: " + player.getName() + " ip: " + hostAddress);
		final Request<?, ?> request = new Request<>(new CmdChannel(), new DisconnectEvent());
		final BaseEvent event = request.getEvent();
		@SuppressWarnings("unchecked")
		final ICommand<BaseEvent> command = (ICommand<BaseEvent>) this.commandFactory.createCommand(request);
		command.execute(event, player);
		connection.close();
		return true;
	}

	public static final class Builder {
		private Map<Player, INonBlockingConnection> connections;

		private Map<INonBlockingConnection, Player> connectedPlayers;

		private ICommandFactory commandFactory;

		private Map<Player, Queue<OutEvent>> toSendMessages;

		public OnDisconnectHandler build() {
			return new OnDisconnectHandler(this);
		}

		public Builder setCommandFactory(final ICommandFactory commandFactory) {
			this.commandFactory = commandFactory;
			return this;
		}

		public Builder setConnectedPlayers(final Map<INonBlockingConnection, Player> connectedPlayers) {
			this.connectedPlayers = connectedPlayers;
			return this;
		}

		public Builder setConnections(final Map<Player, INonBlockingConnection> connections) {
			this.connections = connections;
			return this;
		}

		public Builder setToSendMessages(final Map<Player, Queue<OutEvent>> toSendMessages) {
			this.toSendMessages = toSendMessages;
			return this;
		}
	}
}
