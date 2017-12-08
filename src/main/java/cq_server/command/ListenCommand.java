package cq_server.command;

import cq_server.event.ListenEvent;
import cq_server.model.Player;

public final class ListenCommand implements ICommand<ListenEvent> {
	@Override
	public void execute(final ListenEvent event, final Player player) {
		final Boolean ready = event.getReady();
		player.setReady(ready);
		player.setListen(true);
	}
}
