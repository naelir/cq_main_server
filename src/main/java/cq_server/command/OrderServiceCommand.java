package cq_server.command;

import java.util.Collections;

import cq_server.event.OrderServiceEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public class OrderServiceCommand extends BaseCommand implements ICommand<OrderServiceEvent> {
	public OrderServiceCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final OrderServiceEvent event, final Player player) {
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
