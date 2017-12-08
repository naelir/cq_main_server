package cq_server.command;

import java.util.Collections;

import cq_server.event.WebLoginEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class WebLoginCommand extends BaseCommand implements ICommand<WebLoginEvent> {
	public WebLoginCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final WebLoginEvent event, final Player player) {
		final String name = String.valueOf(event.getUserid());
		final int id = player.getId();
		final String formattedName = this.nameFormatter.format(name, String.valueOf(id));
		player.setName(formattedName);
		player.setLoggedIn(true);
		this.loggedPlayers.put(id, player);
		this.chats.get(0)
			.addUser(player);
		this.userList.add(player);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run();
	}
}
