package cq_server.game;

import java.util.List;

import cq_server.handler.IOutputMessageHandler;

public class Player extends BasePlayer {
	private final IOutputMessageHandler outputMessageHandler;

	public Player(
			final Integer id,
			final String ip,
			final String name,
			final IOutputMessageHandler outputMessageHandler) {
		super(id, ip, name);
		this.outputMessageHandler = outputMessageHandler;

	}

	@Override
	public void handle(final List<Object> data) {
		this.outputMessageHandler.sendMessage(this, data);
	}
}
