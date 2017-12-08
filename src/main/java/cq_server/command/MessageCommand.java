package cq_server.command;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.event.MessageEvent;
import cq_server.game.Game;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public final class MessageCommand extends BaseCommand implements ICommand<MessageEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(MessageCommand.class);

	public MessageCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final MessageEvent event, final Player player) {
		final Game game = this.games.get(player);
		final String message = event.getMessage();
		LOG.info(message);
		game.message(player, message);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
