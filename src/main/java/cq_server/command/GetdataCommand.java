package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;

import cq_server.event.GetDataEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;;

@SuppressWarnings("unused")
public final class GetdataCommand implements ICommand {
	private final IOutputMessageHandler outputMessageHandler;

	private final GetDataEvent event;

	public GetdataCommand(final GetDataEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel(), player.getMydata()));
	}
}
