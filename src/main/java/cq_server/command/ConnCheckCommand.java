package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;

import cq_server.event.ConnectionCheckEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Game;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.ConnStatus;;

public final class ConnCheckCommand implements ICommand {
	@SuppressWarnings("unused")
	private final ConnectionCheckEvent event;

	private final Map<BasePlayer, Game> games;

	private final IOutputMessageHandler outputMessageHandler;

	public ConnCheckCommand(final ConnectionCheckEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.games = notNull("games", builder.games);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Game game = this.games.get(player);
		if (game != null)
			this.outputMessageHandler.sendMessage(player,
					Arrays.asList(player.getCmdChannel(), new ConnStatus(game.getId())));
		else
			this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel(), new ConnStatus()));
	}
}
