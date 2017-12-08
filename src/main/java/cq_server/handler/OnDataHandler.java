package cq_server.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.INonBlockingConnection;

import cq_server.command.ICommand;
import cq_server.factory.ICommandFactory;
import cq_server.factory.IRequestFactory;
import cq_server.model.BaseChannel;
import cq_server.model.BaseEvent;
import cq_server.model.ChannelEvent;
import cq_server.model.Player;
import cq_server.model.Request;

public class OnDataHandler implements IDataHandler {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(DefaultInEventHandler.class);

	private static final String POLICY_FILE_REQUEST = "<policy-file-request/>";

	private static final String POLICY_REQUEST_ANSWER = "<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";

	private final Map<INonBlockingConnection, Player> connectedPlayers;

	private final ICommandFactory commandFactory;

	private final IOutEventHandler outEventHandler;

	private final IRequestFactory requestFactory;

	public OnDataHandler(final Builder builder) {
		this.connectedPlayers = builder.connectedPlayers;
		this.commandFactory = builder.commandFactory;
		this.requestFactory = builder.requestFactory;
		this.outEventHandler = builder.outEventHandler;
	}

	@Override
	public boolean onData(final INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
		final String message = connection.readStringByDelimiter("\0");
		if (POLICY_FILE_REQUEST.equals(message))
			connection.write(POLICY_REQUEST_ANSWER);
		else {
			final Player player = this.connectedPlayers.get(connection);
			final Request<?, ?> request = this.requestFactory.getRequest(message);
			final BaseChannel channel = request.getChannel();
			final BaseEvent event = request.getEvent();
			this.outEventHandler.onChannelEvent(new ChannelEvent(player, channel));
			@SuppressWarnings("unchecked")
			final ICommand<BaseEvent> command = (ICommand<BaseEvent>) this.commandFactory.createCommand(request);
			command.execute(event, player);
		}
		return true;
	}

	public static final class Builder {
		public IRequestFactory requestFactory;

		private Map<INonBlockingConnection, Player> connectedPlayers;

		private ICommandFactory commandFactory;

		private IOutEventHandler outEventHandler;

		public OnDataHandler build() {
			return new OnDataHandler(this);
		}

		public Builder setCommandFactory(final ICommandFactory commandFactory) {
			this.commandFactory = commandFactory;
			return this;
		}

		public Builder setConnectedPlayers(final Map<INonBlockingConnection, Player> connectedPlayers) {
			this.connectedPlayers = connectedPlayers;
			return this;
		}

		public Builder setOutEventHandler(final IOutEventHandler outEventHandler) {
			this.outEventHandler = outEventHandler;
			return this;
		}

		public Builder setRequestFactory(final IRequestFactory requestFactory) {
			this.requestFactory = requestFactory;
			return this;
		}
	}
}
