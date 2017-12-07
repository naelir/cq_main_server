package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.event.ChatMessageEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Chat;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.ChatMsg;
import cq_server.model.NoChat;

public final class ChatMessageCommand implements ICommand {
	private static final Logger LOG = LoggerFactory.getLogger(ChatMessageCommand.class);

	private static final Pattern DEFAULT_CHAT_FILTER = Pattern.compile("^[\\S]{50,80}");

	private static final Pattern CHATMSG_BAN_IP_PATTERN = Pattern
			.compile("##([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})##");

	private final ChatMessageEvent event;

	private final Map<Integer, Chat> chats;

	private final Map<Integer, BasePlayer> loggedPlayers;

	private final IOutputMessageHandler outputMessageHandler;

	private final AtomicBoolean isWhNeedRefresh;

	public ChatMessageCommand(final ChatMessageEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.chats = notNull("chats", builder.chats);
		this.loggedPlayers = notNull("loggedPlayers", builder.loggedPlayers);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
	}

	@Override
	public void execute(final BasePlayer player) {
		final int chatid = player.getActiveChat().getId();
		final Chat chat = this.chats.get(chatid);
		final String msg = this.event.getMsg();
		final Matcher matcher = CHATMSG_BAN_IP_PATTERN.matcher(msg);
		if (matcher.matches()) {
			final String ip = matcher.group(1);
			for (final BasePlayer currentPlayer : this.loggedPlayers.values())
				if (currentPlayer.getIp().equals(ip))
					this.outputMessageHandler.ban(currentPlayer);
		} else {
			chat.addMessage(new ChatMsg(player.getName(), msg));
			LOG.info(msg);
			if (DEFAULT_CHAT_FILTER.matcher(msg).matches()) {
				final Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, calendar.get(1) + 1000);
				player.setNoChat(new NoChat(calendar));
			}
		}
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
		this.isWhNeedRefresh.set(true);
	}
}
