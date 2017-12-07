package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.LogoutEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;

@SuppressWarnings("unused")
public final class LogoutCommand implements ICommand {
	private final LogoutEvent event;

	private final AtomicBoolean isWhNeedRefresh;

	private final IOutputMessageHandler outputMessageHandler;

	public LogoutCommand(final LogoutEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		player.setLoggedIn(false);
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
		this.isWhNeedRefresh.set(true);
	}
}
