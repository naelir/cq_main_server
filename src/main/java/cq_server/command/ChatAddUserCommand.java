package cq_server.command;

import java.util.Collections;
import java.util.Optional;

import cq_server.event.ChatAddUserEvent;
import cq_server.game.Chat;
import cq_server.model.ChatMsg;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class ChatAddUserCommand extends BaseCommand implements ICommand<ChatAddUserEvent> {
	public ChatAddUserCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final ChatAddUserEvent event, final Player player) {
		final Optional<Player> invitedPlayer = this.userList.get(event.getUser());
		final Integer chatId = event.getChatId();
		if (invitedPlayer.isPresent()) {
			final Player invited = invitedPlayer.get();
			if (chatId == -1) {
				final int id = this.idCreator.createId(Chat.class);
				final Chat chat = new Chat(id, player);
				chat.addUser(player);
				chat.addUser(invited);
				player.setActiveChat(chat.getId());
				player.setMstate(chat.getMstate());
				chat.addMessage(new ChatMsg("#1", player.getName()));
				chat.addMessage(new ChatMsg("#1", invited.getName()));
				this.chats.put(chat.getId(), chat);
				this.waithallRefreshTask.run(Collections.unmodifiableSet(chat.getUsers()));
			} else {
				final Chat chat = this.chats.get(chatId);
				chat.addUser(invited);
				chat.addMessage(new ChatMsg("#1", invited.getName()));
				this.waithallRefreshTask.run(Collections.unmodifiableSet(chat.getUsers()));
			}
		}
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
