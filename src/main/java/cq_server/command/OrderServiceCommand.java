package cq_server.command;

import java.util.Arrays;

import cq_server.event.OrderServiceEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;

public class OrderServiceCommand implements ICommand {
	@SuppressWarnings("unused")
	private OrderServiceEvent event;

	private IOutputMessageHandler outputMessageHandler;

	public OrderServiceCommand(final OrderServiceEvent event, final CommandParamsBuilder builder) {
		this.event = event;
		this.outputMessageHandler = builder.outputMessageHandler;
	}

	@Override
	public void execute(final BasePlayer player) {
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
	}
}
