package cq_server.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.factory.IQuestionContextFactory;
import cq_server.handler.IOutEventHandler;
import cq_server.model.AnswerResult;
import cq_server.model.CmdAnswer;
import cq_server.model.CmdSelect;
import cq_server.model.CmdTip;
import cq_server.model.GameFrame;
import cq_server.model.GameHistory;
import cq_server.model.GameOver;
import cq_server.model.GameRoomType;
import cq_server.model.Message;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.Players;
import cq_server.model.Question;
import cq_server.model.QuestionContext;
import cq_server.model.TipInfo;
import cq_server.model.TipQuestion;
import cq_server.model.TipQuestionContext;
import cq_server.model.TipResult;
import cq_server.model.WinnerSelect;

public final class Game {
	private static final Logger LOG = LoggerFactory.getLogger(Game.class);

	private static final int GAME_PLAYERS_BATTLE_COUNT = 2;

	public static final int GAME_PLAYERS_COUNT = 3;

	private final int id;

	private final Map<Player, Integer> players;

	private final GameState state;

	private final GameRoomType type;

	private final GameHistory gameHistory;

	private final IQuestionContextFactory questionContextFactory;

	private final Map<Player, Object> commands;

	private final IOutEventHandler messageHandler;

	public Game(final Builder builder) {
		this.id = builder.id;
		this.players = builder.players;
		this.state = builder.state;
		this.type = builder.type;
		this.gameHistory = builder.gameHistory;
		this.questionContextFactory = builder.questionContextFactory;
		this.messageHandler = builder.messageHandler;
		this.commands = new ConcurrentHashMap<>();
	}

	public void answer(final Player player, final Integer answer) {
		LOG.info("{} answer: {}", player.getName(), answer);
		this.commands.remove(player);
		final AnswerResult answerResult = this.gameHistory.getQhistory()
			.peekLast()
			.getQuestion()
			.getAnswerResult();
		final Integer playerId = this.players.get(player);
		answerResult.setAnswer(playerId, answer);
		this.tryNextFrame();
	}

	public void close(final Player player) {
		this.state.disconnect(this.players.get(player));
	}

	public void disconnect(final Player player, final Player robot) {
		final Integer playerId = this.players.get(player);
		this.state.disconnect(playerId);
		this.players.remove(player);
		this.players.put(robot, playerId);
		LOG.debug("{} switched to robot", player);
		final Object unansweredCommand = this.commands.remove(player);
		final List<Object> messages;
		if (unansweredCommand != null) {
			this.commands.put(robot, unansweredCommand);
			messages = Arrays.asList(unansweredCommand);
		} else
			messages = Collections.emptyList();
		this.messageHandler.onOutEvent(new OutEvent(OutEvent.Kind.LISTEN, robot, messages));
	}

	public Integer getId() {
		return this.id;
	}

	public GameRoomType getType() {
		return this.type;
	}

	private boolean isAllReady() {
		boolean ready = true;
		for (final Player player : this.players.keySet())
			ready = ready && player.isReady();
		LOG.trace("is all ready: {}", ready);
		return ready;
	}

	public void message(final Player player, final String msg) {
		final Integer from = this.players.get(player);
		final Message message = new Message(from, 0, msg);
		for (final Player element : this.players.keySet()) {
			final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, element,
					Arrays.asList(message));
			this.messageHandler.onOutEvent(event);
		}
	}

	private void nextFrame() {
		this.players.keySet()
			.forEach(element -> element.setReady(false));
		switch (this.state.getCurrentFrame()) {
		case START: {
			for (final Map.Entry<Player, Integer> entry : this.players.entrySet()) {
				final Integer playerId = entry.getValue();
				final Player player = entry.getKey();
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player,
						Arrays.asList(this.state, new Players(playerId, this.players)));
				this.messageHandler.onOutEvent(event);
			}
			this.state.setCurrentFrame(GameFrame.PREPARE_BASES);
			break;
		}
		case PREPARE_BASES: {
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			this.state.setCurrentFrame(GameFrame.BUILD_FIRST_BASE);
			break;
		}
		case BUILD_FIRST_BASE: {
			this.state.setOffender(1);
			this.state.addBase(1);
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			this.state.setCurrentFrame(GameFrame.BUILD_SECOND_BASE);
			break;
		}
		case BUILD_SECOND_BASE: {
			this.state.setOffender(2);
			this.state.addBase(2);
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			this.state.setCurrentFrame(GameFrame.BUILD_THIRD_BASE);
			break;
		}
		case BUILD_THIRD_BASE: {
			this.state.setOffender(3);
			this.state.addBase(3);
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			this.state.setCurrentFrame(GameFrame.PREPARE_FOR_TIPQUESTION);
			break;
		}
		case PREPARE_FOR_TIPQUESTION: {
			this.state.setOffender(1);
			this.state.clearSelections();
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			this.state.setCurrentFrame(GameFrame.SEND_SPREADINGTIP);
			break;
		}
		case SEND_SPREADINGTIP: {
			final TipQuestionContext tipQuestionContext = this.questionContextFactory
				.createTipQuestionContext(Game.GAME_PLAYERS_COUNT);
			LOG.debug("{}", tipQuestionContext.getRawTip());
			final TipQuestion tipQuestion = tipQuestionContext.getTipQuestion();
			this.gameHistory.getTiphistory()
				.offer(tipQuestionContext);
			for (final Player player : this.players.keySet()) {
				final CmdTip cmdTip = new CmdTip(Game.GAME_PLAYERS_COUNT);
				final List<Object> messages = Arrays.asList(this.state, cmdTip, tipQuestion);
				this.commands.put(player, cmdTip);
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player, messages);
				this.messageHandler.onOutEvent(event);
			}
			this.state.setCurrentFrame(GameFrame.SEND_SPREADINGTIP_TIPINFO_TIPRESULT);
			break;
		}
		case SEND_SPREADINGTIP_TIPINFO_TIPRESULT: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.getTiphistory()
				.peekLast();
			final TipInfo tipinfo = tipQuestionContext.getTipinfo();
			final TipResult tipResult = tipQuestionContext.getTipResult();
			this.players.keySet()
				.forEach(player -> {
					final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player,
							Arrays.asList(this.state, tipinfo, tipResult));
					this.messageHandler.onOutEvent(event);
				});
			this.state.setCurrentFrame(GameFrame.SEND_SPREADING_SELECTAREA_TO_WINNERTIP_1);
			break;
		}
		case SEND_SPREADING_SELECTAREA_TO_WINNERTIP_1: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.getTiphistory()
				.peekLast();
			final Integer winner = tipQuestionContext.getTipResult()
				.getWinner();
			this.state.setOffender(winner);
			this.sendSelectFirstArea(this.state);
			this.state.setCurrentFrame(GameFrame.SEND_SPREADING_SELECTAREA_TO_WINNERTIP_2);
			break;
		}
		case SEND_SPREADING_SELECTAREA_TO_WINNERTIP_2: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.getTiphistory()
				.peekLast();
			final Integer winner = tipQuestionContext.getTipResult()
				.getWinner();
			this.state.setOffender(winner);
			this.sendSelectNextArea(this.state);
			this.state.setCurrentFrame(GameFrame.SEND_SPREADING_SELECTAREA_TO_SECONDTIP);
			break;
		}
		case SEND_SPREADING_SELECTAREA_TO_SECONDTIP: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.getTiphistory()
				.peekLast();
			final Integer second = tipQuestionContext.getTipResult()
				.getSecond();
			this.state.setOffender(second);
			this.sendSelectNextArea(this.state);
			this.state.setCurrentFrame(GameFrame.SPREADINGTIP_AREAS_SELECTED);
			break;
		}
		case SPREADINGTIP_AREAS_SELECTED: {
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			this.state.nextTipRound();
			this.state.clearSelections();
			if (this.state.isBattle()) {
				this.state.setCurrentFrame(GameFrame.NEW_BATTLE);
				this.state.setBattle(1);
			} else
				this.state.setCurrentFrame(GameFrame.PREPARE_FOR_TIPQUESTION);
			break;
		}
		case NEW_BATTLE: {
			this.state.battleStage();
			this.state.switchBattleOffender();
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			this.state.setCurrentFrame(GameFrame.SEND_SELECT_BATTLE_AREA_TO_OFFENDER);
			break;
		}
		case SEND_SELECT_BATTLE_AREA_TO_OFFENDER: {
			this.state.switchBattleOffender();
			this.sendSelectBattle(this.state);
			this.state.setCurrentFrame(GameFrame.BATTLE_AREA_SELECTED);
			break;
		}
		case BATTLE_AREA_SELECTED: {
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			this.state.setCurrentFrame(GameFrame.SEND_FOUROPTIONSQUESTION_TO_OFFENDER_DEFFENDER);
			break;
		}
		case SEND_FOUROPTIONSQUESTION_TO_OFFENDER_DEFFENDER: {
			final QuestionContext questionContext = this.questionContextFactory
				.createQuestionContext(GAME_PLAYERS_BATTLE_COUNT, this.state.getOffender(),
						this.state.getDeffender());
			LOG.debug("{}", questionContext.getQuestion()
				.getQ());
			final Question question = questionContext.getQuestion();
			this.gameHistory.getQhistory()
				.offer(questionContext);
			this.send4OptionQToOD(this.state, question);
			this.state.setCurrentFrame(GameFrame.SEND_FOUROPTIONSQUESTION_ANSWERRESULT);
			break;
		}
		case SEND_FOUROPTIONSQUESTION_ANSWERRESULT: {
			final AnswerResult answerresult = this.gameHistory.getQhistory()
				.peekLast()
				.getQuestion()
				.getAnswerResult();
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(new OutEvent(OutEvent.Kind.LISTEN,
						player, Arrays.asList(this.state, answerresult))));
			switch (answerresult.getStatus()) {
			case SUCCESS:
				if (this.state.isSelectedAreaTower())
					this.state.setCurrentFrame(GameFrame.DESTROY_TOWER);
				else {
					this.state.occupyAttackedArea();
					this.state.setCurrentFrame(GameFrame.PREPARE_NEXT_LEG);
				}
				break;
			case TIE:
				this.state.setCurrentFrame(GameFrame.SEND_BATTLE_TIPQUESTION);
				break;
			case OFFENDER_FAIL:
				this.state.deffendArea();
				this.state.setCurrentFrame(GameFrame.PREPARE_NEXT_LEG);
				break;
			case BOTH_FAIL:
				this.state.setCurrentFrame(GameFrame.PREPARE_NEXT_LEG);
				break;
			case NA:
				break;
			default:
				break;
			}
			break;
		}
		case SEND_BATTLE_TIPQUESTION: {
			final TipQuestionContext tipQuestionContext = this.questionContextFactory
				.createTipQuestionContext(GAME_PLAYERS_BATTLE_COUNT);
			LOG.debug("{}", tipQuestionContext.getRawTip());
			this.gameHistory.getTiphistory()
				.offer(tipQuestionContext);
			final TipQuestion tipQuestion = tipQuestionContext.getTipQuestion();
			this.sendTipTo2Players(this.state, tipQuestion);
			this.state.setCurrentFrame(GameFrame.SEND_BATTLETIPQUESTION_TIPINFO_TIPRESULT);
			break;
		}
		case SEND_BATTLETIPQUESTION_TIPINFO_TIPRESULT: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.getTiphistory()
				.peekLast();
			final TipInfo tipinfo = tipQuestionContext.getTipinfo();
			final TipResult tipResult = tipQuestionContext.getTipResult();
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(new OutEvent(OutEvent.Kind.LISTEN,
						player, Arrays.asList(this.state, tipinfo, tipResult))));
			if (tipResult.getWinner()
				.equals(this.state.getOffender())) {
				if (this.state.isSelectedAreaTower())
					this.state.setCurrentFrame(GameFrame.DESTROY_TOWER);
				else {
					this.state.occupyAttackedArea();
					this.state.setCurrentFrame(GameFrame.PREPARE_NEXT_LEG);
				}
			} else {
				this.state.deffendArea();
				this.state.setCurrentFrame(GameFrame.PREPARE_NEXT_LEG);
			}
			break;
		}
		case DESTROY_TOWER: {
			this.state.destroyTower();
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			if (this.state.isDeffenderDestroyed()) {
				this.state.setCurrentFrame(GameFrame.PREPARE_NEXT_LEG);
				this.state.decreaseActivePlayers();
			} else
				this.state
					.setCurrentFrame(GameFrame.SEND_FOUROPTIONSQUESTION_TO_OFFENDER_DEFFENDER);
			break;
		}
		case PREPARE_NEXT_LEG: {
			this.players.keySet()
				.forEach(player -> this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(this.state))));
			final int currentBattle = this.state.getRound();
			this.state.clearSelections();
			this.state.prepareNextLeg();
			final int nextBattle = this.state.getRound();
			if (this.state.isFinished())
				this.state.setCurrentFrame(GameFrame.FINISH_GAME);
			else if (nextBattle != currentBattle)
				this.state.setCurrentFrame(GameFrame.NEW_BATTLE);
			else
				this.state.setCurrentFrame(GameFrame.SEND_SELECT_BATTLE_AREA_TO_OFFENDER);
			break;
		}
		case FINISH_GAME: {
			this.sendFinish(this.state);
			this.state.setCurrentFrame(GameFrame.WAITING_FOR_CLOSING);
			break;
		}
		case WAITING_FOR_CLOSING:
			LOG.debug("last frame {}", this.state.getCurrentFrame());
			LOG.debug("points {}", this.state.getPoints());
			break;
		default:
			break;
		}
	}

	public void ready() {
		this.tryNextFrame();
	}

	public void selectArea(final Player player, final int area) {
		LOG.debug("{} select area: {}", player.getName(), area);
		this.commands.remove(player);
		final Integer playerId = this.players.get(player);
		if (this.state.getCurrentFrame()
			.getState() == 4) {
			if (area == 0) {
				final int randomArea = this.state.getRandomArea(playerId);
				LOG.debug("{} auto select area: {}", player.getName(), randomArea);
				this.state.attackArea(playerId, randomArea);
			} else
				this.state.attackArea(playerId, area);
		} else if (area == 0) {
			final int randomArea = this.state.getRandomArea(playerId);
			LOG.debug("{} auto select area: {}", player.getName(), randomArea);
			this.state.occupyEmptyArea(playerId, randomArea);
			this.gameHistory.getSpreadingHistory()
				.offer(new WinnerSelect(playerId, randomArea));
		} else {
			this.state.occupyEmptyArea(playerId, area);
			this.gameHistory.getSpreadingHistory()
				.offer(new WinnerSelect(playerId, area));
		}
		this.tryNextFrame();
	}

	private void send4OptionQToOD(final GameState state, final Question question) {
		LOG.debug("offender: {}, deffender {}", state.getOffender(), state.getDeffender());
		for (final Map.Entry<Player, Integer> entry : this.players.entrySet()) {
			final Integer playerId = entry.getValue();
			final Player player = entry.getKey();
			if (playerId.equals(state.getOffender()) || playerId.equals(state.getDeffender())) {
				final CmdAnswer cmdAnswer = new CmdAnswer();
				final List<Object> messages = Arrays.asList(state, cmdAnswer, question);
				this.commands.put(player, cmdAnswer);
				this.messageHandler
					.onOutEvent(new OutEvent(OutEvent.Kind.LISTEN, player, messages));
			} else
				this.messageHandler.onOutEvent(
						new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(state, question)));
		}
	}

	private void sendFinish(final GameState state) {
		final String placings = StringUtils.join(state.getCurrentRanking());
		final Map<Integer, Player> inverse = new HashMap<>(Game.GAME_PLAYERS_COUNT);
		for (final Entry<Player, Integer> entry : this.players.entrySet())
			inverse.put(entry.getValue(), entry.getKey());
		for (final Entry<Player, Integer> entry : this.players.entrySet()) {
			//@formatter:off
			final GameOver gameOver =
					new GameOver.Builder()
					.setAnsCnt(100)
					.setAoj(100)
					.setDivision(1)
					.setGoodAnsCount(100)
					.setGwr(100)
					.setIam(entry.getValue())
					.setJepChange(0)
					.setMoney(0)
					.setMoneyChange(0)
					.setName1(inverse.get(1).getName())
					.setName2(inverse.get(2).getName())
					.setName3(inverse.get(3).getName())
					.setNewJep(0)
					.setPlacings(placings)
					.setSelVep(0.0)
					.setSelVepCh(0.0)
					.setTipCnt(100)
					.setTipVep(0.0)
					.setTipVepCh(0.0)
					.setVep(0)
					.setVepTipCnt(0.0)
					.build();
			//@formatter:on
			final Player player = entry.getKey();
			this.messageHandler.onOutEvent(
					new OutEvent(OutEvent.Kind.LISTEN, player, Arrays.asList(state, gameOver)));
		}
	}

	private void sendSelectBattle(final GameState state) {
		for (final Map.Entry<Player, Integer> entry : this.players.entrySet()) {
			final Integer playerId = entry.getValue();
			final Player player = entry.getKey();
			if (playerId.equals(state.getOffender())) {
				final Set<Integer> available = state.getAvailableAreas(playerId);
				final CmdSelect cmdSelect = new CmdSelect(available, 0);
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player,
						Arrays.asList(state, cmdSelect));
				this.commands.put(player, cmdSelect);
				this.messageHandler.onOutEvent(event);
			} else {
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player,
						Arrays.asList(state));
				this.messageHandler.onOutEvent(event);
			}
		}
	}

	private void sendSelectFirstArea(final GameState state) {
		for (final Entry<Player, Integer> entry : this.players.entrySet()) {
			final Integer playerId = entry.getValue();
			final Player player = entry.getKey();
			if (playerId.equals(state.getOffender())) {
				final Set<Integer> available = state.getAvailableAreas(playerId);
				final CmdSelect cmdSelect = new CmdSelect(available, 0);
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player,
						Arrays.asList(state, cmdSelect));
				this.commands.put(player, cmdSelect);
				this.messageHandler.onOutEvent(event);
			} else {
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player,
						Arrays.asList(state));
				this.messageHandler.onOutEvent(event);
			}
		}
	}

	private void sendSelectNextArea(final GameState state) {
		for (final Map.Entry<Player, Integer> entry : this.players.entrySet()) {
			final WinnerSelect winnerSelect = this.gameHistory.getSpreadingHistory()
				.getLast();
			final Integer playerId = entry.getValue();
			final Player player = entry.getKey();
			if (playerId.equals(state.getOffender())) {
				final Set<Integer> available = state.getAvailableAreas(playerId);
				final CmdSelect cmdSelect = new CmdSelect(available, 0);
				final List<Object> messages = Arrays.asList(state, cmdSelect, winnerSelect);
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player, messages);
				this.commands.put(player, cmdSelect);
				this.messageHandler.onOutEvent(event);
			} else {
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player,
						Arrays.asList(state, winnerSelect));
				this.messageHandler.onOutEvent(event);
			}
		}
	}

	private void sendTipTo2Players(final GameState state, final TipQuestion tipQuestion) {
		for (final Entry<Player, Integer> entry : this.players.entrySet()) {
			final Integer playerId = entry.getValue();
			final Player player = entry.getKey();
			if (playerId.equals(state.getOffender()) || playerId.equals(state.getDeffender())) {
				final CmdTip cmdTip = new CmdTip(2);
				final List<Object> messages = Arrays.asList(state, cmdTip, tipQuestion);
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player, messages);
				this.commands.put(player, cmdTip);
				this.messageHandler.onOutEvent(event);
			} else {
				final List<Object> messages = Arrays.asList(state, tipQuestion);
				final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, player, messages);
				this.messageHandler.onOutEvent(event);
			}
		}
	}

	public void start() {
		this.tryNextFrame();
	}

	public void tip(final Player player, final int tip) {
		LOG.debug("{} tip: {}", player.getName(), tip);
		this.commands.remove(player);
		final int playerId = this.players.get(player);
		final TipQuestionContext tipQuestionContext = this.gameHistory.getTiphistory()
			.peekLast();
		final TipInfo tipinfo = tipQuestionContext.getTipinfo();
		final TipResult tipResult = tipQuestionContext.getTipResult();
		tipinfo.setTip(playerId, tip);
		if (tipinfo.isReady()) {
			tipinfo.calculate();
			tipResult.setResults(tipinfo.getAnswer(), tipinfo.getTipAnswers());
		} else
			this.players.keySet()
				.forEach(element -> {
					final OutEvent event = new OutEvent(OutEvent.Kind.LISTEN, element,
							Arrays.asList(tipinfo));
					this.messageHandler.onOutEvent(event);
				});
		this.tryNextFrame();
	}

	@Override
	public String toString() {
		return this.state.toString();
	}

	private void tryNextFrame() {
		if (this.isAllReady() && this.commands.isEmpty())
			this.nextFrame();
	}

	public static final class Builder {
		int id;

		Map<Player, Integer> players;

		GameState state;

		GameRoomType type;

		GameHistory gameHistory;

		IQuestionContextFactory questionContextFactory;

		IOutEventHandler messageHandler;

		public Game build() {
			return new Game(this);
		}

		public Builder setGameHistory(final GameHistory gameHistory) {
			this.gameHistory = gameHistory;
			return this;
		}

		public Builder setId(final int id) {
			this.id = id;
			return this;
		}

		public Builder setMessageHandler(final IOutEventHandler messageHandler) {
			this.messageHandler = messageHandler;
			return this;
		}

		public Builder setPlayers(final Map<Player, Integer> players) {
			this.players = players;
			return this;
		}

		public Builder setQuestionContextFactory(
				final IQuestionContextFactory questionContextFactory) {
			this.questionContextFactory = questionContextFactory;
			return this;
		}

		public Builder setState(final GameState state) {
			this.state = state;
			return this;
		}

		public Builder setType(final GameRoomType type) {
			this.type = type;
			return this;
		}
	}

	public enum Stage {
		SPREADING, BATTLE, LAST_ROUND;
	}
}
