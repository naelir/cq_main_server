package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;

import cq_server.event.SelectAreaEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Game;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.BaseChannel;;

public final class SelectCommand implements ICommand {
	private final SelectAreaEvent event;

	private final Map<BasePlayer, Game> games;

	private final IOutputMessageHandler outputMessageHandler;

	public SelectCommand(final SelectAreaEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.games = notNull("games", builder.games);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Game game = this.games.get(player);
		final BaseChannel cmdChannel = player.getCmdChannel();
		final Integer area = this.event.getArea();
		player.setReady(true);
		game.selectArea(player, area);
		game.tryNextFrame();
		this.outputMessageHandler.sendMessage(player, Arrays.asList(cmdChannel));
	}
}
