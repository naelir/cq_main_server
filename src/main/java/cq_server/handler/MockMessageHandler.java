package cq_server.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.util.List;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.INonBlockingConnection;

import cq_server.game.BasePlayer;

public class MockMessageHandler implements IInputMessageHandler, IOutputMessageHandler {
	@Override
	public boolean ban(final BasePlayer player) {
		return true;
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
	public boolean sendMessage(final BasePlayer player, final List<Object> data) {
		return true;
	}
}
