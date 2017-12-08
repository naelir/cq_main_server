package cq_server.command;

import java.util.Arrays;

import cq_server.event.ModFriendListEvent;
import cq_server.model.FriendListChange;
import cq_server.model.ModFriendOperation;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class ModFriendListCommand extends BaseCommand implements ICommand<ModFriendListEvent> {
	public ModFriendListCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final ModFriendListEvent event, final Player player) {
		final String user = event.getUser();
		player.setFriendList(user, ModFriendOperation.FRIEND);
		this.outEventHandler
			.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Arrays.asList(new FriendListChange(user))));
	}
}
