package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;

import cq_server.event.ModFriendListEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.FriendListChange;
import cq_server.model.ModFriendOperation;;

public final class ModFriendListCommand implements ICommand {
	private final ModFriendListEvent event;

	private final IOutputMessageHandler outputMessageHandler;

	public ModFriendListCommand(final ModFriendListEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final String user = this.event.getUser();
		player.setFriendList(user, ModFriendOperation.FRIEND); 
		this.outputMessageHandler.sendMessage(player,
				Arrays.asList(player.getCmdChannel(), new FriendListChange(user)));
	}
}
