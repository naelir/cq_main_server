package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.SetActiveChatEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;;

public final class SetActiveChatCommand implements ICommand {
	private final SetActiveChatEvent event;

	private final AtomicBoolean isWhNeedRefresh;

	private final IOutputMessageHandler outputMessageHandler;

	public SetActiveChatCommand(final SetActiveChatEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Integer chatId = this.event.getChatId();
		final Integer mstate = this.event.getMstate();
		player.setActiveChat(chatId);
		player.setMstate(mstate);
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
		this.isWhNeedRefresh.set(true);
	}
}
