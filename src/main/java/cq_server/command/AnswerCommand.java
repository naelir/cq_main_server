package cq_server.command;

import java.util.Collections;

import cq_server.event.AnswerEvent;
import cq_server.game.Game;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class AnswerCommand extends BaseCommand implements ICommand<AnswerEvent> {
	public AnswerCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final AnswerEvent event, final Player player) {
		final Game game = this.games.get(player);
		final Integer answer = event.getAnswer();
		player.setReady(true);
		game.answer(player, answer);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
