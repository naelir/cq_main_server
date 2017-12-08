package cq_server.command;

import java.util.Collections;

import cq_server.event.RateQuestionEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public class RateQuestionCommand extends BaseCommand implements ICommand<RateQuestionEvent> {
	public RateQuestionCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final RateQuestionEvent event, final Player player) {
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
