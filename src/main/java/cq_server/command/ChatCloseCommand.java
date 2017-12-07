package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.ChatCloseEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Chat;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.BaseChannel;
import cq_server.model.ChatMsg;;

public final class ChatCloseCommand implements ICommand {
	private final ChatCloseEvent event;

	private final Map<Integer, Chat> chats;

	private final AtomicBoolean isWhNeedRefresh;

	private final IOutputMessageHandler outputMessageHandler;

	public ChatCloseCommand(final ChatCloseEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.chats = notNull("chats", builder.chats);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Integer activeChat = this.event.getActiveChat();
		final Integer mstate = this.event.getMstate();
		final Integer chatId = this.event.getChatId();
		final BaseChannel cmdChannel = player.getCmdChannel();
		player.setActiveChat(activeChat);
		player.setMstate(mstate);
		final Chat chat = this.chats.get(chatId);
		chat.remove(player);
		if (chat.isEmpty())
			this.chats.remove(chatId);
		chat.addMessage(new ChatMsg("#2", player.getName()));
		this.outputMessageHandler.sendMessage(player, Arrays.asList(cmdChannel));
		this.isWhNeedRefresh.set(true);
	}
}
