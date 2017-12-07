package cq_server.game;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cq_server.handler.MockMessageHandler;
import cq_server.model.CmdAnswer;
import cq_server.model.CmdSelect;
import cq_server.model.CmdTip;

public class Robot extends BasePlayer {
	private static final int RANDOM_READY_DELAY_MILLIS = 100;

	private static final int MIN_READY_DELAY_MILLIS = 50;

	private static final int RANDOM_ANSWER_DELAY_MILLIS = 3000;

	private static final int MIN_ANSWER_DELAY_MILLIS = 1000;

	private final Game game;

	private final MockMessageHandler messageHandler;

	private final Random generator;

	private final ScheduledExecutorService scheduler;

	public Robot(
			final Integer id,
			final String ip,
			final String name,
			final Game game,
			final ScheduledExecutorService scheduler) {
		super(id, ip, name);
		this.game = game;
		this.scheduler = scheduler;
		this.messageHandler = new MockMessageHandler();
		this.generator = new Random();
	}

	@Override
	public void handle(final List<Object> data) {
		this.messageHandler.sendMessage(this, data);
		final int readyDelay = this.generator.nextInt(RANDOM_READY_DELAY_MILLIS) + MIN_READY_DELAY_MILLIS;
		this.scheduler.schedule(() -> {
			this.setReady(true);
			this.game.tryNextFrame(); 
		}, readyDelay, TimeUnit.MILLISECONDS);
		final int answerDelay = this.generator.nextInt(RANDOM_ANSWER_DELAY_MILLIS) + MIN_ANSWER_DELAY_MILLIS;
		this.scheduler.schedule(() -> {
			for (final Object object : data)
				if (object instanceof CmdTip) {
					final int result = this.generator.nextInt(2016);
					this.game.tip(this, result);
				} else if (object instanceof CmdAnswer) {
					final int answer = this.generator.nextInt(4) + 1;
					this.game.answer(this, answer);
				} else if (object instanceof CmdSelect) {
					final Integer area = ((CmdSelect) object).getAvailableAreas().stream().findAny().get();
					this.game.selectArea(this, area);
				}
			this.game.tryNextFrame();
		}, answerDelay, TimeUnit.MILLISECONDS);
	}
}
