package cq_server.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.INonBlockingConnection;

import cq_server.command.ICommand;
import cq_server.event.DisconnectEvent;
import cq_server.event.validator.IEventValidator;
import cq_server.factory.ICommandFactory;
import cq_server.factory.IMessageFactory;
import cq_server.factory.IPlayerFactory;
import cq_server.game.BasePlayer;
import cq_server.model.BaseChannel;
import cq_server.model.BaseEvent;
import cq_server.model.NetworkMessage;

public final class InputMessageHandler implements IInputMessageHandler {
	private static final Logger LOG = LoggerFactory.getLogger(InputMessageHandler.class);

	private static final String POLICY_FILE_REQUEST = "<policy-file-request/>";

	private static final String POLICY_REQUEST_ANSWER = "<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";

	private final List<String> bannedIps;

	private final Map<BasePlayer, INonBlockingConnection> connections;

	private final Map<INonBlockingConnection, BasePlayer> connectedPlayers;

	private final ICommandFactory commandFactory;

	private final IPlayerFactory playerFactory;

	private final IMessageFactory messageFactory;

	private final IEventValidator eventValidator;

	public InputMessageHandler(final Builder builder) {
		this.bannedIps = builder.bannedIps;
		this.connections = builder.connections;
		this.connectedPlayers = builder.connectedPlayers;
		this.commandFactory = builder.commandFactory;
		this.playerFactory = builder.playerFactory;
		this.messageFactory = builder.messageFactory;
		this.eventValidator = builder.eventValidator;
	}

	@Override
	public boolean onConnect(final INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
		final String hostAddress = connection.getRemoteAddress().getHostAddress();
		if (this.bannedIps.contains(hostAddress))
			connection.close();
		else {
			final BasePlayer player = this.playerFactory.createPlayer(hostAddress);
			this.connections.put(player, connection);
			this.connectedPlayers.put(connection, player);
			LOG.info("onConnect from ip: " + hostAddress);
		}
		return true;
	}

	@Override
	public boolean onData(final INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
		final String message = connection.readStringByDelimiter("\0");
		if (POLICY_FILE_REQUEST.equals(message))
			connection.write(POLICY_REQUEST_ANSWER);
		else {
			final BasePlayer player = this.connectedPlayers.get(connection);
			final NetworkMessage networkMessage = this.messageFactory.createInputNetworkMessage(message);
			final BaseChannel channel = networkMessage.getChannel();
			final BaseEvent event = networkMessage.getEvent();
			final BaseEvent validatedEvent = this.eventValidator.validate(event);
			player.setChannel(channel);
			final ICommand command = this.commandFactory.createCommand(validatedEvent);
			command.execute(player);
		}
		return true;
	}

	@Override
	public boolean onDisconnect(final INonBlockingConnection connection) throws IOException {
		final String hostAddress = connection.getRemoteAddress().getHostAddress();
		final BasePlayer player = this.connectedPlayers.get(connection);
		this.connections.remove(player);
		LOG.warn("onDisconnect player: " + player.getName() + " ip: " + hostAddress);
		final ICommand command = this.commandFactory.createCommand(new DisconnectEvent());
		command.execute(player);
		connection.close();
		return true;
	}

	@Override
	public boolean onIdleTimeout(final INonBlockingConnection connection) throws IOException {
		final String hostAddress = connection.getRemoteAddress().getHostAddress();
		final BasePlayer player = this.connectedPlayers.get(connection);
		this.connections.remove(player);
		LOG.warn("onIdleTimeout player: " + player.getName() + " ip: " + hostAddress);
		final ICommand command = this.commandFactory.createCommand(new DisconnectEvent());
		command.execute(player);
		connection.close();
		return true;
	}

	public static final class Builder {
		private List<String> bannedIps;

		private Map<BasePlayer, INonBlockingConnection> connections;

		private Map<INonBlockingConnection, BasePlayer> connectedPlayers;

		private ICommandFactory commandFactory;

		private IPlayerFactory playerFactory;

		private IMessageFactory messageFactory;

		private IEventValidator eventValidator;

		public InputMessageHandler build() {
			return new InputMessageHandler(this);
		}

		public Builder setBannedIps(final List<String> bannedIps) {
			this.bannedIps = bannedIps;
			return this;
		}

		public Builder setCommandFactory(final ICommandFactory commandFactory) {
			this.commandFactory = commandFactory;
			return this;
		}

		public Builder setConnectedPlayers(final Map<INonBlockingConnection, BasePlayer> connectedPlayers) {
			this.connectedPlayers = connectedPlayers;
			return this;
		}

		public Builder setConnections(final Map<BasePlayer, INonBlockingConnection> connections) {
			this.connections = connections;
			return this;
		}

		public Builder setEventValidator(final IEventValidator eventValidator) {
			this.eventValidator = eventValidator;
			return this;
		}

		public Builder setMessageFactory(final IMessageFactory messageFactory) {
			this.messageFactory = messageFactory;
			return this;
		}

		public Builder setPlayerFactory(final IPlayerFactory playerFactory) {
			this.playerFactory = playerFactory;
			return this;
		}
	}
}