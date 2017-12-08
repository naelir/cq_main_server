package cq_server.command;

import java.util.Collections;

import cq_server.event.LogoutEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class LogoutCommand extends BaseCommand implements ICommand<LogoutEvent> {
	public LogoutCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final LogoutEvent event, final Player player) {
		player.setLoggedIn(false);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run();
	}
}
