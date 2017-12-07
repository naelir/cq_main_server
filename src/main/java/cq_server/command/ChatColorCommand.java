package cq_server.command;

import java.util.Arrays;

import cq_server.event.ChatColorEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;

public class ChatColorCommand implements ICommand {
	@SuppressWarnings("unused")
	private ChatColorEvent event;

	private IOutputMessageHandler outputMessageHandler;

	public ChatColorCommand(final ChatColorEvent event, final CommandParamsBuilder builder) {
		this.event = event;
		this.outputMessageHandler = builder.outputMessageHandler;
	}

	@Override
	public void execute(final BasePlayer player) {
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
	}
}
