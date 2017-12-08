package cq_server.command;

import java.util.Collections;

import cq_server.event.ReadyEvent;
import cq_server.game.Game;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class ReadyCommand extends BaseCommand implements ICommand<ReadyEvent> {
	public ReadyCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final ReadyEvent event, final Player player) {
		final Game game = this.games.get(player);
		player.setReady(true);
		game.ready();
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
