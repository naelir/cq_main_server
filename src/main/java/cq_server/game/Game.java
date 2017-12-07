package cq_server.game;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.factory.IPlayerFactory;
import cq_server.factory.IQuestionContextFactory;
import cq_server.model.*;

public final class Game {
	private static final Logger LOG = LoggerFactory.getLogger(Game.class);

	private static final int GAME_PLAYERS_BATTLE_COUNT = 2;

	public static final int GAME_PLAYERS_COUNT = 3;

	private final AtomicInteger commandsReceived;

	private final int id;

	private final Map<BasePlayer, Integer> players;

	private final GameState state;

	private final GameRoomType type;

	private final GameHistory gameHistory;

	private final IQuestionContextFactory questionContextFactory;

	private final IPlayerFactory playerFactory;

	public Game(
			final int id,
			final Map<BasePlayer, Integer> players,
			final GameRoomType type,
			final GameState state,
			final GameHistory gameHistory,
			final IQuestionContextFactory questionContextFactory,
			final IPlayerFactory playerFactory) {
		this.id = id;
		this.players = players;
		this.type = type;
		this.state = state;
		this.gameHistory = gameHistory;
		this.questionContextFactory = questionContextFactory;
		this.playerFactory = playerFactory;
		this.commandsReceived = new AtomicInteger(0);
	}

	public void answer(final BasePlayer player, final int answer) {
		LOG.info("{} answer: {}", player.getName(), answer);
		this.commandsReceived.incrementAndGet();
		final AnswerResult answerResult = this.gameHistory.peekLastQuestion().getAnswerResult();
		final Integer playerId = this.players.get(player);
		answerResult.setAnswer(playerId, answer);
	}

	public void close(final BasePlayer player) {
		this.state.disconnect(this.players.get(player));
	}

	public void disconnect(final BasePlayer player) {
		final Integer playerId = this.players.get(player);
		this.state.disconnect(playerId);
		final String name = player.getName();
		final BasePlayer robot = this.playerFactory.createRobot(playerId, name, this);
		robot.setReady(true);
		this.players.remove(player);
		this.players.put(robot, playerId);
		LOG.debug("{} switched to robot", player);
		switch (this.state.getCurrentFrame()) {
		case SEND_BATTLETIPQUESTION_TIPINFO:
		case SEND_SPREADINGTIP_TIPINFO: {
			if (!this.gameHistory.isPlayerSendTip(playerId))
				this.tip(robot, 0);
			break;
		}
		case BATTLE_AREA_SELECTED: {
			if (playerId.equals(this.state.getOffender()) && !this.state.isPlayerSelectArea(playerId)) {
				final int area = this.state.getRandomArea(playerId);
				this.selectArea(robot, area);
			}
			break;
		}
		case SEND_SPREADING_SELECTAREA_TO_WINNERTIP_2:
		case SEND_SPREADING_SELECTAREA_TO_SECONDTIP:
		case SPREADINGTIP_AREAS_SELECTED: {
			if (playerId.equals(this.state.getOffender()) && !this.state.isPlayerSelectArea(playerId)) {
				final int area = this.state.getRandomArea(playerId);
				this.selectArea(robot, area);
			}
			break;
		}
		case SEND_FOUROPTIONSQUESTION_ANSWERRESULT: {
			final AnswerResult answerresult = this.gameHistory.peekLastQuestion().getAnswerResult();
			if ((playerId.equals(this.state.getOffender()) || playerId.equals(this.state.getDeffender()))
					&& !answerresult.isAnswered(playerId))
				this.answer(robot, 1);
			break;
		}
		default:
			break;
		}
		this.tryNextFrame();
	}

	public Integer getId() {
		return this.id;
	}

	public GameRoomType getType() {
		return this.type;
	}

	private boolean isAllReady() {
		boolean ready = true;
		for (final BasePlayer player : this.players.keySet())
			ready = ready && player.isReady() && player.getListenChannel() != null;
		return ready;
	}

	public boolean isFinished() {
		boolean isEmpty = true;
		for (final BasePlayer player : this.players.keySet())
			isEmpty = isEmpty && (player instanceof Robot);
		return isEmpty;
	}

	public void message(final BasePlayer player, final String msg) {
		final Integer from = this.players.get(player);
		final Message message = new Message(from, 0, msg);
		final List<Object> command = Arrays.asList(message);
		for (final BasePlayer key : this.players.keySet())
			this.onGameStateChange(key, command);
	}

	private void moveOnNextFrame() {
		this.commandsReceived.set(0);
		for (final BasePlayer player : this.players.keySet())
			player.setReady(false);
		this.nextFrame();
	}

	// very shitty, but thats the way how it works
	private void nextFrame() {
		switch (this.state.getCurrentFrame()) {
		case START: {
			this.players.entrySet().forEach(player -> this.onGameStateChange(player.getKey(),
					Arrays.asList(this.state, new Players(player.getValue(), this.players))));
			this.state.setCurrentFrame(GameFrame.PREPARE_BASES);
			break;
		}
		case PREPARE_BASES: {
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			this.state.setCurrentFrame(GameFrame.BUILD_FIRST_BASE);
			break;
		}
		case BUILD_FIRST_BASE: {
			this.state.setOffender(1);
			this.state.addBase(1);
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			this.state.setCurrentFrame(GameFrame.BUILD_SECOND_BASE);
			break;
		}
		case BUILD_SECOND_BASE: {
			this.state.setOffender(2);
			this.state.addBase(2);
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			this.state.setCurrentFrame(GameFrame.BUILD_THIRD_BASE);
			break;
		}
		case BUILD_THIRD_BASE: {
			this.state.setOffender(3);
			this.state.addBase(3);
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			this.state.setCurrentFrame(GameFrame.PREPARE_FOR_TIPQUESTION);
			break;
		}
		case PREPARE_FOR_TIPQUESTION: {
			this.state.setOffender(1);
			this.state.clearSelections();
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			this.state.setCurrentFrame(GameFrame.SEND_SPREADINGTIP);
			break;
		}
		case SEND_SPREADINGTIP: {
			final TipQuestionContext tipQuestionContext = this.questionContextFactory
					.createTipQuestionContext(Game.GAME_PLAYERS_COUNT);
			LOG.debug("{}", tipQuestionContext.getRawTip());
			final TipQuestion tipQuestion = tipQuestionContext.getTipQuestion();
			final List<Object> currentState = Arrays.asList(this.state, new CmdTip(Game.GAME_PLAYERS_COUNT),
					tipQuestion);
			this.gameHistory.offerTip(tipQuestionContext);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			this.state.setCurrentFrame(GameFrame.SEND_SPREADINGTIP_TIPINFO);
			break;
		}
		case SEND_SPREADINGTIP_TIPINFO: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.peekLastTip();
			final TipInfo tipinfo = tipQuestionContext.getTipinfo();
			if (!tipinfo.isReady()) {
				final List<Object> currentState = Arrays.asList(tipinfo);
				this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
				break;
			} else
				this.state.setCurrentFrame(GameFrame.SEND_SPREADINGTIP_TIPINFO_TIPRESULT);
		}
		case SEND_SPREADINGTIP_TIPINFO_TIPRESULT: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.peekLastTip();
			final TipInfo tipinfo = tipQuestionContext.getTipinfo();
			final TipResult tipResult = tipQuestionContext.getTipResult();
			final List<Object> currentState = Arrays.asList(this.state, tipinfo, tipResult);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			this.state.setCurrentFrame(GameFrame.SEND_SPREADING_SELECTAREA_TO_WINNERTIP_1);
			break;
		}
		case SEND_SPREADING_SELECTAREA_TO_WINNERTIP_1: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.peekLastTip();
			final Integer winner = tipQuestionContext.getTipResult().getWinner();
			this.state.setOffender(winner);
			this.sendSelectFirstArea(this.state);
			this.state.setCurrentFrame(GameFrame.SEND_SPREADING_SELECTAREA_TO_WINNERTIP_2);
			break;
		}
		case SEND_SPREADING_SELECTAREA_TO_WINNERTIP_2: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.peekLastTip();
			final Integer winner = tipQuestionContext.getTipResult().getWinner();
			this.state.setOffender(winner);
			this.sendSelectNextArea(this.state);
			this.state.setCurrentFrame(GameFrame.SEND_SPREADING_SELECTAREA_TO_SECONDTIP);
			break;
		}
		case SEND_SPREADING_SELECTAREA_TO_SECONDTIP: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.peekLastTip();
			final Integer second = tipQuestionContext.getTipResult().getSecond();
			this.state.setOffender(second);
			this.sendSelectNextArea(this.state);
			this.state.setCurrentFrame(GameFrame.SPREADINGTIP_AREAS_SELECTED);
			break;
		}
		case SPREADINGTIP_AREAS_SELECTED: {
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
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
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
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
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			this.state.setCurrentFrame(GameFrame.SEND_FOUROPTIONSQUESTION_TO_OFFENDER_DEFFENDER);
			break;
		}
		case SEND_FOUROPTIONSQUESTION_TO_OFFENDER_DEFFENDER: {
			final QuestionContext questionContext = this.questionContextFactory.createQuestionContext(
					GAME_PLAYERS_BATTLE_COUNT, this.state.getOffender(), this.state.getDeffender());
			LOG.debug("{}", questionContext.getRawQuestion());
			final Question question = questionContext.getQuestion();
			this.gameHistory.offerQuestion(questionContext);
			this.send4OptionQToOD(this.state, question);
			this.state.setCurrentFrame(GameFrame.SEND_FOUROPTIONSQUESTION_ANSWERRESULT);
			break;
		}
		case SEND_FOUROPTIONSQUESTION_ANSWERRESULT: {
			final AnswerResult answerresult = this.gameHistory.peekLastQuestion().getAnswerResult();
			final List<Object> currentState = Arrays.asList(this.state, answerresult);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
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
			}
			break;
		}
		case SEND_BATTLE_TIPQUESTION: {
			final TipQuestionContext tipQuestionContext = this.questionContextFactory
					.createTipQuestionContext(GAME_PLAYERS_BATTLE_COUNT);
			LOG.debug("{}", tipQuestionContext.getRawTip());
			this.gameHistory.offerTip(tipQuestionContext);
			final TipQuestion tipQuestion = tipQuestionContext.getTipQuestion();
			this.sendTipTo2Players(this.state, tipQuestion);
			this.state.setCurrentFrame(GameFrame.SEND_BATTLETIPQUESTION_TIPINFO);
			break;
		}
		case SEND_BATTLETIPQUESTION_TIPINFO: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.peekLastTip();
			final TipInfo tipinfo = tipQuestionContext.getTipinfo();
			if (tipinfo.isReady())
				this.state.setCurrentFrame(GameFrame.SEND_BATTLETIPQUESTION_TIPINFO_TIPRESULT);
			else {
				final List<Object> currentState = Arrays.asList(tipinfo);
				this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
				break;
			}
		}
		case SEND_BATTLETIPQUESTION_TIPINFO_TIPRESULT: {
			final TipQuestionContext tipQuestionContext = this.gameHistory.peekLastTip();
			final TipInfo tipinfo = tipQuestionContext.getTipinfo();
			final TipResult tipResult = tipQuestionContext.getTipResult();
			final List<Object> currentState = Arrays.asList(this.state, tipinfo, tipResult);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			if (tipResult.getWinner().equals(this.state.getOffender())) {
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
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
			if (this.state.isDeffenderDestroyed()) {
				this.state.setCurrentFrame(GameFrame.PREPARE_NEXT_LEG);
				this.state.decreaseActivePlayers();
			} else
				this.state.setCurrentFrame(GameFrame.SEND_FOUROPTIONSQUESTION_TO_OFFENDER_DEFFENDER);
			break;
		}
		case PREPARE_NEXT_LEG: {
			final List<Object> currentState = Arrays.asList(this.state);
			this.players.keySet().forEach(player -> this.onGameStateChange(player, currentState));
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

	private void onGameStateChange(final BasePlayer player, final List<Object> o) {
		final BaseChannel channel = player.getListenChannel();
		if (channel != null) {
			final List<Object> list = new ArrayList<>();
			list.add(channel);
			list.addAll(o);
			player.handle(list);
		}
	}

	public void selectArea(final BasePlayer player, final int area) {
		LOG.debug("{} select area: {}", player.getName(), area);
		this.commandsReceived.incrementAndGet();
		final Integer playerId = this.players.get(player);
		if (this.state.getCurrentFrame().getState() == 4) {
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
			this.gameHistory.offerWinner(new WinnerSelect(playerId, randomArea));
		} else {
			this.state.occupyEmptyArea(playerId, area);
			this.gameHistory.offerWinner(new WinnerSelect(playerId, area));
		}
	}

	private void send4OptionQToOD(final GameState state, final Question question) {
		LOG.debug("offender: {}, deffender {}", state.getOffender(), state.getDeffender());
		for (final Map.Entry<BasePlayer, Integer> entry : this.players.entrySet()) {
			final Integer playerId = entry.getValue();
			if (playerId.equals(state.getOffender()) || playerId.equals(state.getDeffender())) {
				final List<Object> currentState = Arrays.asList(state, new CmdAnswer(), question);
				this.onGameStateChange(entry.getKey(), currentState);
			} else {
				final List<Object> currentState = Arrays.asList(state, question);
				this.onGameStateChange(entry.getKey(), currentState);
			}
		}
	}

	private void sendFinish(final GameState state) {
		final String placings = StringUtils.join(state.getCurrentRanking());
		final Map<Integer, BasePlayer> inverse = new HashMap<>(Game.GAME_PLAYERS_COUNT);
		for (final Entry<BasePlayer, Integer> entry : this.players.entrySet())
			inverse.put(entry.getValue(), entry.getKey());
		for (final Entry<BasePlayer, Integer> entry : this.players.entrySet()) {
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
			final List<Object> command = Arrays.asList(state, gameOver);
			this.onGameStateChange(entry.getKey(), command);
		}
	}

	private void sendSelectBattle(final GameState state) {
		for (final Map.Entry<BasePlayer, Integer> entry : this.players.entrySet()) {
			final Integer playerId = entry.getValue();
			if (playerId.equals(state.getOffender())) {
				final Set<Integer> available = state.getAvailableAreas(playerId);
				final List<Object> command = Arrays.asList(state, new CmdSelect(available, 0));
				this.onGameStateChange(entry.getKey(), command);
			} else {
				final List<Object> command = Arrays.asList(state);
				this.onGameStateChange(entry.getKey(), command);
			}
		}
	}

	private void sendSelectFirstArea(final GameState state) {
		for (final Entry<BasePlayer, Integer> entry : this.players.entrySet()) {
			final Integer playerId = entry.getValue();
			if (playerId.equals(state.getOffender())) {
				final Set<Integer> available = state.getAvailableAreas(playerId);
				final List<Object> ssttw1o = Arrays.asList(state, new CmdSelect(available, 0));
				this.onGameStateChange(entry.getKey(), ssttw1o);
			} else {
				final List<Object> ssttw1 = Arrays.asList(state);
				this.onGameStateChange(entry.getKey(), ssttw1);
			}
		}
	}

	private void sendSelectNextArea(final GameState state) {
		for (final Map.Entry<BasePlayer, Integer> entry : this.players.entrySet()) {
			final WinnerSelect winnerSelect = this.gameHistory.peekLastWinner();
			final Integer playerId = entry.getValue();
			if (playerId.equals(state.getOffender())) {
				final Set<Integer> available = state.getAvailableAreas(playerId);
				final List<Object> ssttw1o = Arrays.asList(state, new CmdSelect(available, 0), winnerSelect);
				this.onGameStateChange(entry.getKey(), ssttw1o);
			} else {
				final List<Object> ssttw1 = Arrays.asList(state, winnerSelect);
				this.onGameStateChange(entry.getKey(), ssttw1);
			}
		}
	}

	private void sendTipTo2Players(final GameState state, final TipQuestion tipQuestion) {
		for (final Entry<BasePlayer, Integer> entry : this.players.entrySet()) {
			final Integer playerId = entry.getValue();
			if (playerId.equals(state.getOffender()) || playerId.equals(state.getDeffender())) {
				final List<Object> command = Arrays.asList(state, new CmdTip(2), tipQuestion);
				this.onGameStateChange(entry.getKey(), command);
			} else {
				final List<Object> command = Arrays.asList(state, tipQuestion);
				this.onGameStateChange(entry.getKey(), command);
			}
		}
	}

	public void tip(final BasePlayer player, final int tip) {
		LOG.debug("{} tip: {}", player.getName(), tip);
		this.commandsReceived.incrementAndGet();
		final int playerId = this.players.get(player);
		final TipQuestionContext tipQuestionContext = this.gameHistory.peekLastTip();
		final TipInfo tipinfo = tipQuestionContext.getTipinfo();
		final TipResult tipResult = tipQuestionContext.getTipResult();
		tipinfo.setTipInfo(playerId, tip);
		if (tipinfo.isReady()) {
			tipinfo.calculate();
			tipResult.setResults(tipinfo.getAnswer(), tipinfo.getTipAnswers());
		}
	}

	@Override
	public String toString() {
		return this.state.toString();
	}

	public void tryNextFrame() {
		final boolean allReady = this.isAllReady();
		if (allReady)
			switch (this.state.getCurrentFrame()) {
			case START:
			case PREPARE_BASES:
			case BUILD_FIRST_BASE:
			case BUILD_SECOND_BASE:
			case BUILD_THIRD_BASE:
			case PREPARE_FOR_TIPQUESTION:
			case SEND_SPREADINGTIP:
			case SEND_SPREADING_SELECTAREA_TO_WINNERTIP_1:
			case NEW_BATTLE:
			case SEND_SELECT_BATTLE_AREA_TO_OFFENDER:
			case SEND_FOUROPTIONSQUESTION_TO_OFFENDER_DEFFENDER:
			case SEND_BATTLE_TIPQUESTION:
			case PREPARE_NEXT_LEG:
			case DESTROY_TOWER:
			case FINISH_GAME:
			case WAITING_FOR_CLOSING:
				if (this.commandsReceived.get() == 0)
					this.moveOnNextFrame();
				break;
			case SEND_FOUROPTIONSQUESTION_ANSWERRESULT:
				if (this.commandsReceived.get() == 2)
					this.moveOnNextFrame();
				break;
			default:
				if (this.commandsReceived.get() == 1)
					this.moveOnNextFrame();
				break;
			}
	}
}
