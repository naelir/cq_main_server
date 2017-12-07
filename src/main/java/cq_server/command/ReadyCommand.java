package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;

import cq_server.event.ReadyEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Game;
import cq_server.handler.IOutputMessageHandler;;

@SuppressWarnings("unused")
public final class ReadyCommand implements ICommand {
	private final ReadyEvent event;

	private final Map<BasePlayer, Game> games;

	private final IOutputMessageHandler outputMessageHandler;

	public ReadyCommand(final ReadyEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.games = notNull("games", builder.games);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Game game = this.games.get(player);
		player.setReady(true);
		game.tryNextFrame();
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
	}
}
