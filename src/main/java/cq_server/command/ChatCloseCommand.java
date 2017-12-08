package cq_server.command;

import java.util.Collections;

import cq_server.event.ChatCloseEvent;
import cq_server.game.Chat;
import cq_server.model.ChatMsg;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class ChatCloseCommand extends BaseCommand implements ICommand<ChatCloseEvent> {
	public ChatCloseCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final ChatCloseEvent event, final Player player) {
		final Integer activeChat = event.getActiveChat();
		final Integer mstate = event.getMstate();
		final Integer chatId = event.getChatId();
		player.setActiveChat(activeChat);
		player.setMstate(mstate);
		final Chat chat = this.chats.get(chatId);
		chat.remove(player);
		if (chat.isEmpty())
			this.chats.remove(chatId);
		chat.addMessage(new ChatMsg("#2", player.getName()));
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run(Collections.unmodifiableSet(chat.getUsers()));
	}
}
