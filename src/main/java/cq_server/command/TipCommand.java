package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;

import cq_server.event.TipEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Game;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.BaseChannel;;

public final class TipCommand implements ICommand {
	private final TipEvent event;

	private final Map<BasePlayer, Game> games;

	private final IOutputMessageHandler outputMessageHandler;

	public TipCommand(final TipEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.games = notNull("games", builder.games);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Game game = this.games.get(player);
		final Integer tip = this.event.getTip();
		final BaseChannel cmdChannel = player.getCmdChannel();
		player.setReady(true);
		game.tip(player, tip);
		game.tryNextFrame();
		this.outputMessageHandler.sendMessage(player, Arrays.asList(cmdChannel));
	}
}
