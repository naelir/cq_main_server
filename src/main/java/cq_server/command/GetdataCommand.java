package cq_server.command;

import java.util.Arrays;

import cq_server.event.GetDataEvent;
import cq_server.model.MyData;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class GetdataCommand extends BaseCommand implements ICommand<GetDataEvent> {
	public GetdataCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final GetDataEvent event, final Player player) {
		final MyData mydata = player.getMydata();
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Arrays.asList(mydata)));
	}
}
