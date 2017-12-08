package cq_server.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;

public final class DefaultInEventHandler implements IInEventHandler {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(DefaultInEventHandler.class);

	private final IDataHandler dataHandler;

	private final IDisconnectHandler disconnectHandler;

	private final IConnectHandler connectHandler;

	public DefaultInEventHandler(final IDataHandler dataHandler, final IDisconnectHandler disconnectHandler,
			final IConnectHandler connectHandler) {
		super();
		this.dataHandler = dataHandler;
		this.disconnectHandler = disconnectHandler;
		this.connectHandler = connectHandler;
	}

	@Override
	public boolean onConnect(final INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
		return this.connectHandler.onConnect(connection);
	}

	@Override
	public boolean onData(final INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
		return this.dataHandler.onData(connection);
	}

	@Override
	public boolean onDisconnect(final INonBlockingConnection connection) throws IOException {
		return this.disconnectHandler.onDisconnect(connection);
	}

	@Override
	public boolean onIdleTimeout(final INonBlockingConnection connection) throws IOException {
		return this.disconnectHandler.onDisconnect(connection);
	}
}