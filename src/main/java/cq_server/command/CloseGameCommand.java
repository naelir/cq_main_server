package cq_server.command;

import java.util.Collections;

import cq_server.event.CloseGameEvent;
import cq_server.game.Game;
import cq_server.model.OutEvent;
import cq_server.model.Page;
import cq_server.model.Player;

public final class CloseGameCommand extends BaseCommand implements ICommand<CloseGameEvent> {
	public CloseGameCommand(final cq_server.command.BaseCommand.Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final CloseGameEvent event, final Player player) {
		final Game game = this.games.remove(player);
		player.setPage(Page.WAITHALL);
		game.close(player);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run();
	}
}
