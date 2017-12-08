package cq_server.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.INonBlockingConnection;

import cq_server.model.ChannelEvent;
import cq_server.model.OutEvent;

public class MockMessageHandler implements IInEventHandler, IOutEventHandler {
	@Override
	public void onChannelEvent(final ChannelEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onConnect(final INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onData(final INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDisconnect(final INonBlockingConnection connection) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onIdleTimeout(final INonBlockingConnection connection) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onOutEvent(final OutEvent event) {
		// TODO Auto-generated method stub
	}
}
