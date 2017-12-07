package cq_server.task;

import java.util.Map;

import cq_server.game.BasePlayer;
import cq_server.game.Game;

public class FinishedGamesClearerTask implements Runnable {

	private final Map<BasePlayer, Game> games;

	public FinishedGamesClearerTask(final Map<BasePlayer, Game> games) {
		this.games = games;
	}

	@Override
	public void run() {
		this.games.entrySet().removeIf(a -> a.getValue().isFinished());
	}
}
