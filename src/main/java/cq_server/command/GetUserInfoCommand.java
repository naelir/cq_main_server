package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;

import cq_server.event.GetUserInfoEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.UserInfo;;

public final class GetUserInfoCommand implements ICommand {

	private final GetUserInfoEvent event;

	private final IOutputMessageHandler outputMessageHandler;

	public GetUserInfoCommand(final GetUserInfoEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final String user = this.event.getUser();
		final UserInfo info = new UserInfo(user); 
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel(), info));

	}
}
