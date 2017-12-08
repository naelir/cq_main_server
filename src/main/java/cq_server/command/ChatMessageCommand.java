package cq_server.command;

import java.util.Calendar;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.event.ChatMessageEvent;
import cq_server.game.Chat;
import cq_server.model.ChatMsg;
import cq_server.model.NoChat;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class ChatMessageCommand extends BaseCommand implements ICommand<ChatMessageEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(ChatMessageCommand.class);

	private static final Pattern DEFAULT_CHAT_FILTER = Pattern.compile("^[\\S]{50,80}");

	private static final Pattern CHATMSG_BAN_IP_PATTERN = Pattern
		.compile("##([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})##");

	public ChatMessageCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final ChatMessageEvent event, final Player player) {
		final int chatid = player.getActiveChat()
			.getId();
		final Chat chat = this.chats.get(chatid);
		final String msg = event.getMsg();
		final Matcher matcher = CHATMSG_BAN_IP_PATTERN.matcher(msg);
		if (matcher.matches()) {
			final String ip = matcher.group(1);
			for (final Player currentPlayer : this.loggedPlayers.values())
				if (currentPlayer.getIp()
					.equals(ip))
					this.banHandler.ban(currentPlayer);
		} else {
			chat.addMessage(new ChatMsg(player.getName(), msg));
			LOG.info(msg);
			if (DEFAULT_CHAT_FILTER.matcher(msg)
				.matches()) {
				final Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, calendar.get(1) + 1000);
				player.setNoChat(new NoChat(calendar));
			}
		}
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run(Collections.unmodifiableSet(chat.getUsers()));
	}
}
