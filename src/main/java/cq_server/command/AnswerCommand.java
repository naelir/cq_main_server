package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;

import cq_server.event.AnswerEvent;
import cq_server.game.BasePlayer;
import cq_server.game.Game;
import cq_server.handler.IOutputMessageHandler;;

public final class AnswerCommand implements ICommand {
	private final AnswerEvent event;

	private final IOutputMessageHandler outputMessageHandler;

	private final Map<BasePlayer, Game> games;

	public AnswerCommand(final AnswerEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
		this.games = notNull("games", builder.games);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Game game = this.games.get(player);
		final Integer answer = this.event.getAnswer();
		player.setReady(true);
		game.answer(player, answer);
		game.tryNextFrame();
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
	}
}
