package cq_server.command;

import java.util.Collections;

import cq_server.event.SelectAreaEvent;
import cq_server.game.Game;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class SelectCommand extends BaseCommand implements ICommand<SelectAreaEvent> {
	public SelectCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final SelectAreaEvent event, final Player player) {
		final Game game = this.games.get(player);
		final Integer area = event.getArea();
		player.setReady(true);
		game.selectArea(player, area);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
