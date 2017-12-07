package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.CloseGameEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Game;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.Page;;

public final class CloseGameCommand implements ICommand {
	@SuppressWarnings("unused")
	private final CloseGameEvent event;

	private final IOutputMessageHandler outputMessageHandler;

	private final AtomicBoolean isWhNeedRefresh;

	private final Map<BasePlayer, Game> games;

	public CloseGameCommand(final CloseGameEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
		this.games = notNull("games", builder.games);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Game game = this.games.remove(player);
		player.setPage(Page.WAITHALL);
		game.close(player);
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
		this.isWhNeedRefresh.set(true);
	}
}
