package cq_server.factory;

import java.util.concurrent.ScheduledExecutorService;

import cq_server.game.*;
import cq_server.handler.IOutputMessageHandler;

public class PlayerFactory implements IPlayerFactory {

	private final IOutputMessageHandler outputMessageHandler;

	private final IdFactory idCreator;

	private final ScheduledExecutorService scheduler;

	public PlayerFactory(
			final IdFactory idCreator,
			final IOutputMessageHandler outputMessageHandler,
			final ScheduledExecutorService scheduler) {
		this.idCreator = idCreator;
		this.outputMessageHandler = outputMessageHandler;
		this.scheduler = scheduler;
	}

	@Override
	public BasePlayer createPlayer(final String hostAddress) {
		final int id = this.idCreator.createId(BasePlayer.class);
		return new Player(id, hostAddress, String.valueOf(id), this.outputMessageHandler);
	}

	@Override
	public BasePlayer createRobot(final Integer id, final String name, final Game game) {
		return new Robot(id, null, name, game, this.scheduler);
	}
}
