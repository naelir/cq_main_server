package cq_server.command;

import java.util.Collections;

import cq_server.event.DisconnectEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class DefaultCommand extends BaseCommand implements ICommand<DisconnectEvent> {
	public DefaultCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final DisconnectEvent event, final Player player) {
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
