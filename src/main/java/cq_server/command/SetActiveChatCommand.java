package cq_server.command;

import java.util.Arrays;
import java.util.Collections;

import cq_server.event.SetActiveChatEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class SetActiveChatCommand extends BaseCommand implements ICommand<SetActiveChatEvent> {
	public SetActiveChatCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final SetActiveChatEvent event, final Player player) {
		final Integer chatId = event.getChatId();
		final Integer mstate = event.getMstate();
		player.setActiveChat(chatId);
		player.setMstate(mstate);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run(Arrays.asList(player));
	}
}
