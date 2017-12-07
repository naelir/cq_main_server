package cq_server.factory;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cq_server.game.*;
import cq_server.model.GameRoomType;
import cq_server.model.QuestionContext;
import cq_server.model.TipQuestionContext;
import cq_server.model.WinnerSelect;

public class GameFactory implements IGameFactory {
	private final int playersCount;

	private final IdFactory idCreator;

	private final IQuestionContextFactory questionContextFactory;

	private final IPlayerFactory playerFactory;

	private final Map<Integer, List<Integer>> mapBgNeighbors;

	public GameFactory(
			final IdFactory idCreator,
			final IQuestionContextFactory questionContextFactory,
			final IPlayerFactory playerFactory,
			final Map<Integer, List<Integer>> mapBgNeighbors) {
		this.idCreator = idCreator;
		this.questionContextFactory = questionContextFactory;
		this.playerFactory = playerFactory;
		this.mapBgNeighbors = mapBgNeighbors;
		this.playersCount = Game.GAME_PLAYERS_COUNT;
	}

	@Override
	public Game createGame(final List<BasePlayer> players, final GameRoomType gameType) {
		final GameState gameState = new GameState(this.playersCount, new AreasManager(this.mapBgNeighbors));
		final Map<BasePlayer, Integer> gamePlayers = new ConcurrentHashMap<>(this.playersCount);
		for (int i = 0; i < players.size(); i++)
			gamePlayers.put(players.get(i), i + 1);
		final Deque<WinnerSelect> spreadingHistory = new LinkedList<>();
		final Deque<QuestionContext> qhistory = new LinkedList<>();
		final Deque<TipQuestionContext> tiphistory = new LinkedList<>();
		final GameHistory gameHistory = new GameHistory(spreadingHistory, qhistory, tiphistory);
		final int id = this.idCreator.createId(Game.class);
		final Game game = new Game(id, gamePlayers, gameType, gameState, gameHistory, this.questionContextFactory,
				this.playerFactory);
		for (int i = players.size(); i < this.playersCount; i++) {
			final BasePlayer robot = this.playerFactory.createRobot(i + 1, String.format("robot-%d", i + 1), game);
			gamePlayers.put(robot, i + 1);
		}
		return game;
	}
}
