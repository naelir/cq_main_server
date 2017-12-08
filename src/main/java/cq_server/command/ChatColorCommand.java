package cq_server.command;

import java.util.Collections;

import cq_server.event.ChatColorEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public class ChatColorCommand extends BaseCommand implements ICommand<ChatColorEvent> {
	public ChatColorCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final ChatColorEvent event, final Player player) {
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
