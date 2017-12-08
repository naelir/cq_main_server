package cq_server.handler;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cq_server.game.Game;
import cq_server.model.ChannelEvent;
import cq_server.model.CmdAnswer;
import cq_server.model.CmdSelect;
import cq_server.model.CmdTip;
import cq_server.model.GameOver;
import cq_server.model.OutEvent;
import cq_server.model.Player;

public class RobotHandler implements IOutEventHandler {
	private static final int RANDOM_READY_DELAY_MILLIS = 100;

	private static final int MIN_READY_DELAY_MILLIS = 50;

	private static final int RANDOM_ANSWER_DELAY_MILLIS = 3000;

	private static final int MIN_ANSWER_DELAY_MILLIS = 1000;

	private final Map<Player, Game> games;

	private final ScheduledExecutorService scheduler;

	private final Random generator;

	public RobotHandler(final Map<Player, Game> games, final ScheduledExecutorService scheduler) {
		this.games = games;
		this.scheduler = scheduler;
		this.generator = new Random();
	}

	@Override
	public void onChannelEvent(final ChannelEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onOutEvent(final OutEvent event) {
		final Player player = event.getPlayer();
		switch (event.getKind()) {
		case LISTEN:
			final List<Object> messages = event.getMessages();
			player.setListen(false);
			final Game game = this.games.get(player);
			final int readyDelay = this.generator.nextInt(RANDOM_READY_DELAY_MILLIS) + MIN_READY_DELAY_MILLIS;
			final Runnable readyTask = () -> {
				player.setReady(true);
				player.setListen(true);
				game.ready();
			};
			final int answerDelay = this.generator.nextInt(RANDOM_ANSWER_DELAY_MILLIS) + MIN_ANSWER_DELAY_MILLIS;
			final Runnable answerTask = () -> {
				for (final Object object : messages)
					if (object instanceof CmdTip) {
						final int result = this.generator.nextInt(2016);
						game.tip(player, result);
						break;
					} else if (object instanceof CmdAnswer) {
						final int answer = this.generator.nextInt(4) + 1;
						game.answer(player, answer);
						break;
					} else if (object instanceof CmdSelect) {
						final Integer area = ((CmdSelect) object).getAvailableAreas()
							.stream()
							.findAny()
							.get();
						game.selectArea(player, area);
						break;
					} else if (object instanceof GameOver) {
						this.games.remove(player);
						break;
					}
			};
			this.scheduler.schedule(readyTask, readyDelay, TimeUnit.MILLISECONDS);
			this.scheduler.schedule(answerTask, answerDelay, TimeUnit.MILLISECONDS);
			break;
		default:
			break;
		}
	}
}
