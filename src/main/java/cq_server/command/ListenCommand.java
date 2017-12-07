package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.ListenEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Game;;

public final class ListenCommand implements ICommand {
	private final ListenEvent event;

	private final Map<BasePlayer, Game> games;

	private final AtomicBoolean isWhNeedRefresh;

	public ListenCommand(final ListenEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.games = notNull("games", builder.games);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Boolean ready = this.event.getReady();
		player.setReady(ready);
		if (player.getListenChannel().getMsgnum().equals(0))
			this.isWhNeedRefresh.set(true);
		final Game game = this.games.get(player);
		if (game != null)
			game.tryNextFrame();
	}
}
