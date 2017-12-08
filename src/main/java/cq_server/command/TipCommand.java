package cq_server.command;

import java.util.Collections;

import cq_server.event.TipEvent;
import cq_server.game.Game;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class TipCommand extends BaseCommand implements ICommand<TipEvent> {
	public TipCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final TipEvent event, final Player player) {
		final Game game = this.games.get(player);
		final Integer tip = event.getTip();
		player.setReady(true);
		game.tip(player, tip);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
