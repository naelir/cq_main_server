package cq_server.command;

import java.util.Arrays;

import cq_server.event.ConnectionCheckEvent;
import cq_server.game.Game;
import cq_server.model.ConnStatus;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class ConnCheckCommand extends BaseCommand implements ICommand<ConnectionCheckEvent> {
	public ConnCheckCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final ConnectionCheckEvent event, final Player player) {
		final Game game = this.games.get(player);
		if (game != null)
			this.outEventHandler
				.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Arrays.asList(new ConnStatus(game.getId()))));
		else
			this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Arrays.asList(new ConnStatus())));
	}
}
