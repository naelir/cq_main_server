package cq_server.command;

import java.util.Collections;

import cq_server.event.BadQMarkEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public class BadQMarkCommand extends BaseCommand implements ICommand<BadQMarkEvent> {
	public BadQMarkCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final BadQMarkEvent event, final Player player) {
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
