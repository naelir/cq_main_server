package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.ChatAddUserEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Chat;
import cq_server.game.IdFactory;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.ChatMsg;
import cq_server.model.UserList;

public final class ChatAddUserCommand implements ICommand {
	private final AtomicBoolean isWhNeedRefresh;

	private final IOutputMessageHandler outputMessageHandler;

	private final IdFactory idCreator;

	private final ChatAddUserEvent event;

	private final Map<Integer, Chat> chats;

	private final UserList userList;

	public ChatAddUserCommand(final ChatAddUserEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
		this.idCreator = notNull("idCreator", builder.idCreator);
		this.chats = notNull("chats", builder.chats);
		this.userList = notNull("userList", builder.usersList);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Optional<BasePlayer> invitedPlayer = this.userList.get(this.event.getUser());
		final Integer chatId = this.event.getChatId();
		if (invitedPlayer.isPresent()) {
			final BasePlayer invited = invitedPlayer.get();
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
			} else {
				final Chat chat = this.chats.get(chatId);
				chat.addUser(invited);
				chat.addMessage(new ChatMsg("#1", invited.getName()));
			}
		}
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
		this.isWhNeedRefresh.set(true);
	}
}
