package cq_server.factory;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import cq_server.game.Game;
import cq_server.game.GameState;
import cq_server.handler.IOutEventHandler;
import cq_server.model.Area;
import cq_server.model.AreaType;
import cq_server.model.GameFrame;
import cq_server.model.GameHistory;
import cq_server.model.GameRoomType;
import cq_server.model.Player;
import cq_server.model.QuestionContext;
import cq_server.model.Screen;
import cq_server.model.TipQuestionContext;
import cq_server.model.WinnerSelect;

public class DefaultGameFactory implements IGameFactory {
	private final IdFactory idCreator;

	private final IQuestionContextFactory questionContextFactory;

	private final Map<Integer, List<Integer>> mapBgNeighbors;

	private final IOutEventHandler messageHandler;

	public DefaultGameFactory(final IdFactory idCreator,
			final IQuestionContextFactory questionContextFactory,
			final Map<Integer, List<Integer>> mapBgNeighbors,
			final IOutEventHandler messageHandler) {
		this.idCreator = idCreator;
		this.questionContextFactory = questionContextFactory;
		this.mapBgNeighbors = mapBgNeighbors;
		this.messageHandler = messageHandler;
	}

	//@formatter:off
	@Override
	public Game createGame(final List<Player> humans, final GameRoomType type) {
		final Map<Integer, Area> areas = new ConcurrentHashMap<>(this.mapBgNeighbors.size());
		for (int i = 0; i < this.mapBgNeighbors.size(); i++)
			areas.put(i + 1, new Area(i + 1, AreaType.EMPTY, 0, 0));
		final int playersCount = Game.GAME_PLAYERS_COUNT;
		final GameState gameState = new GameState.Builder()
				.setAreas(areas)
				.setBases(this.getMap(playersCount))
				.setBpts(this.getMap(playersCount))
				.setCurrentFrame(GameFrame.START)
				.setDefender(0)
				.setHumanConnected(new CopyOnWriteArrayList<>(Arrays.asList(1, 2, 3)))
				.setIsGameFinished(false)
				.setLeg(1)
				.setNeighbors(this.mapBgNeighbors)
				.setOffender(0)
				.setPlayers(playersCount)
				.setPoints(this.getMap(playersCount))
				.setRandom(new Random())
				.setRound(1)
				.setScreen(Screen.MAP_BG)
				.setSelectedArea(0)
				.setSelections(this.getMap(playersCount))
				.setTipRound(1)
				.setWarOrder(null)
				.build();
		final Map<Player, Integer> players = new ConcurrentHashMap<>(playersCount);
		for (int i = 0; i < humans.size(); i++)
			players.put(humans.get(i), i + 1);
		final Deque<WinnerSelect> spreadingHistory = new LinkedList<>();
		final Deque<QuestionContext> qhistory = new LinkedList<>();
		final Deque<TipQuestionContext> tiphistory = new LinkedList<>();
		final GameHistory gameHistory = new GameHistory(spreadingHistory, qhistory, tiphistory);
		final int id = this.idCreator.createId(Game.class);
		final Game game = new Game.Builder()
			.setGameHistory(gameHistory)
			.setId(id)
			.setMessageHandler(this.messageHandler)
 			.setPlayers(players)
			.setQuestionContextFactory(this.questionContextFactory)
			.setState(gameState)
			.setType(type)
			.build();
		return game;
	}

	private  Map<Integer, AtomicInteger> getMap(final int players) {
		final Map<Integer, AtomicInteger> map = new ConcurrentHashMap<>(players);
		for (int i = 0; i < players; i++)
			map.put(i + 1, new AtomicInteger(0));
		return map;
	}
}
