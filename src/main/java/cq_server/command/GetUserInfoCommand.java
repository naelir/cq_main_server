package cq_server.command;

import java.util.Arrays;

import cq_server.event.GetUserInfoEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.UserInfo;

public final class GetUserInfoCommand extends BaseCommand implements ICommand<GetUserInfoEvent> {
	public GetUserInfoCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final GetUserInfoEvent event, final Player player) {
		final String user = event.getUser();
		final UserInfo info = new UserInfo(user);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Arrays.asList(info)));
	}
}
