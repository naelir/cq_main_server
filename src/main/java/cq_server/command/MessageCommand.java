package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.event.MessageEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Game;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.BaseChannel;;

public final class MessageCommand implements ICommand {
	private static final Logger LOG = LoggerFactory.getLogger(MessageCommand.class);

	private final MessageEvent event;

	private final Map<BasePlayer, Game> games;

	private final IOutputMessageHandler outputMessageHandler;

	public MessageCommand(final MessageEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.games = notNull("games", builder.games);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Game game = this.games.get(player);
		final String message = this.event.getMessage();
		LOG.info(message);
		game.message(player, message);
		final BaseChannel cmdChannel = player.getCmdChannel();
		this.outputMessageHandler.sendMessage(player, Arrays.asList(cmdChannel));
	}
}
