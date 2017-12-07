package cq_server.game;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.Assertions;
import cq_server.model.Area;
import cq_server.model.AreaType;
import cq_server.model.GameFrame;
import cq_server.model.Screen;

/*
 * <STATE SCREEN="MAP_E2" STATE="4" PHASE="2" ROUND="1" CP="2,2" HC="123"
 * WO="123231312444" POINTS="2000,1600,2000" BPTS="0,0,0" BASES="074C05"
 * SEL="000000" AREAS="43434141134311414343411242424142" />
 */
@XmlRootElement(name = "STATE")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GameState {
	private static final Logger LOG = LoggerFactory.getLogger(GameState.class);

	private static final int CASTLE_STATE = 1;

	private static final int CASTLE_VALUE = 1000;

	private static final int DEFEND_AREA_AWARD_POINTS = 100;

	private static final int MINUS_TOWER_BASE_VALUE_CORRECTION = 64;

	private static final int TOWER_DESTROYED_VALUE = 192;

	private static final int MAP_BG_TIP_ROUNDS = 5;

	private static final int MAP_BG_BATTLE_ROUNDS = 4;

	private static final int DEFAULT_AREA_STATE = 4;

	private static final int OCCUPIED_AREA_STATE = 2;

	private static final int OCCUPIED_AREA_VALUE = 400;

	private static final int DEFAULT_AREA_VALUE = 200;

	private static final List<Integer> DEFAULT_WARORDER = Arrays.asList(1, 2, 3, 2, 3, 1, 3, 1, 2, 4, 4, 4);

	private final AreasManager areasManager;

	private final Map<Integer, AtomicInteger> bases;

	private final Map<Integer, AtomicInteger> bpts;

	private GameFrame currentFrame;

	private int defender;

	private final List<Integer> humanConnected;

	private boolean isGameFinished;

	private int leg;

	private int offender;

	private int players;

	private final Map<Integer, AtomicInteger> points;

	private int round;

	private final Screen screen;

	private final Map<Integer, AtomicInteger> selections;

	private int selectedArea;

	private int tipRound;

	private List<Integer> warOrder;

	public GameState() {
		this(Game.GAME_PLAYERS_COUNT, new AreasManager(Collections.emptyMap()));
	}

	public GameState(final int players, final AreasManager areas) {
		super();
		this.players = players;
		this.areasManager = areas;
		this.isGameFinished = false;
		this.screen = Screen.MAP_BG;
		this.points = this.getMap(players);
		this.bpts = this.getMap(players);
		this.bases = this.getMap(players);
		this.selections = this.getMap(players);
		this.offender = 0;
		this.defender = 0;
		this.humanConnected = new CopyOnWriteArrayList<>(Arrays.asList(1, 2, 3));
		this.currentFrame = GameFrame.START;
		this.selectedArea = 0;
		this.tipRound = 1;
		this.leg = 1;
		this.round = 1;
	}

	public void addBase(final Integer player) {
		Assertions.notNull("player", player);
		final Area area = this.areasManager.findBase();
		area.setCode(CASTLE_STATE * 10 + player);
		area.setType(AreaType.CASTLE);
		area.setValue(CASTLE_VALUE);
		final int areaId = area.getId();
		this.bases.get(player).set(areaId);
		final int updatedPoints = this.points.get(player).get() + CASTLE_VALUE;
		this.points.get(player).set(updatedPoints);
		this.selections.get(player).set(areaId);
	}

	public void attackArea(final int me, final int areaId) {
		this.selectedArea = areaId;
		this.selections.get(me).set(areaId);
		this.defender = this.areasManager.get(areaId).getCode() % 10;
		this.offender = me;
	}

	public void battleStage() {
		if (this.warOrder == null)
			this.warOrder = DEFAULT_WARORDER;
	}

	public void clearSelections() {
		this.selections.values().stream().forEach(element -> element.set(0));
	}

	public void decreaseActivePlayers() {
		this.players--;
	}

	public void deffendArea() {
		final int pointsUpdated = this.points.get(this.defender).get() + DEFEND_AREA_AWARD_POINTS;
		this.points.get(this.defender).set(pointsUpdated);
	}

	public void destroyTower() {
		final int baseUpdated = this.bases.get(this.defender).get() + MINUS_TOWER_BASE_VALUE_CORRECTION;
		this.bases.get(this.defender).set(baseUpdated);
		// if 3 towers destroyed, all areas and points moves to offender
		if (baseUpdated > TOWER_DESTROYED_VALUE) {
			this.areasManager.switchOwner(this.defender, this.offender);
			final int allPoints = this.points.get(this.offender).get() + this.points.get(this.defender).get();
			this.points.get(this.offender).set(allPoints);
			this.points.get(this.defender).set(0);
		}
	}

	public void disconnect(final Integer playerId) {
		this.humanConnected.remove(playerId);
	}

	@XmlAttribute(name = "AREAS")
	public String getAreas() {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.areasManager.size(); i++)
			builder.append(String.format("%02d", this.areasManager.get(i + 1).getCode()));
		return builder.toString();
	}

	//@formatter:off
	public Set<Integer> getAvailableAreas(final int player) {
		if (this.tipRound <= MAP_BG_TIP_ROUNDS)
			return this.areasManager
					.findSelectableEmptyNeighborsInTipStage(player)
					.stream()
					.map(element -> element.getId())
					.collect(Collectors.toSet());
		else if (this.round < MAP_BG_BATTLE_ROUNDS)
			return this.areasManager
					.findAttackableAreasInBattleStage(player)
					.stream()
					.map(element -> element.getId())
					.collect(Collectors.toSet());
		else
			return this.areasManager
					.findAttackableAreasInFinalStage(player)
					.stream()
					.map(element -> element.getId())
					.collect(Collectors.toSet());
	}
	//@formatter:on

	@XmlAttribute(name = "BASES")
	public String getBases() {
		final StringBuilder builder = new StringBuilder(36);
		for (int i = 0; i < Game.GAME_PLAYERS_COUNT; i++)
			builder.append(String.format("%02X", this.bases.get(i + 1).get()));
		return builder.toString();
	}

	private Integer getBattleOffender(final int battle, final int leg) {
		final int index = 3 * (battle - 1) + leg - 1;
		final Integer offender = this.warOrder.get(index);
		LOG.debug("next battle offender: " + offender);
		return offender;
	}

	@XmlAttribute(name = "BPTS")
	public String getBpts() {
		return "0,0,0";
	}

	@XmlAttribute(name = "CP")
	public String getCP() {
		return String.format("%d,%d", this.leg, this.offender);
	}

	@XmlTransient
	public GameFrame getCurrentFrame() {
		return this.currentFrame;
	}

	//@formatter:off
	public Integer[] getCurrentRanking() {
		final Integer[] ranking = this.points.keySet().toArray(new Integer[0]);
		final Comparator<Integer> cmp = (o1, o2) -> Integer.compare(this.points.get(o1).get(), this.points.get(o2).get());
		final Comparator<Integer> reverseOrder = Collections.reverseOrder(cmp);
		Arrays.sort(ranking, reverseOrder);
		return ranking;
	}
	//@formatter:on

	public int getDeffender() {
		return this.defender;
	}

	@XmlAttribute(name = "HC")
	public String getHumanConnected() {
		return StringUtils.join(this.humanConnected, "");
	}

	private Map<Integer, AtomicInteger> getMap(final int players) {
		final Map<Integer, AtomicInteger> map = new ConcurrentHashMap<>(players);
		for (int i = 0; i < players; i++)
			map.put(i + 1, new AtomicInteger(0));
		return map;
	}

	@XmlTransient
	public int getOffender() {
		return this.offender;
	}

	@XmlAttribute(name = "PHASE")
	public int getPhase() {
		return this.getCurrentFrame().getPhase();
	}

	@XmlAttribute(name = "POINTS")
	public String getPoints() {
		return String.format("%d,%d,%d", this.points.get(1).get(), this.points.get(2).get(), this.points.get(3).get());
	}

	public int getRandomArea(final int player) {
		if (this.tipRound <= MAP_BG_TIP_ROUNDS)
			return this.areasManager.findSelectableEmptyNeighborsInTipStage(player).stream().findAny().get().getId();
		else
			return this.areasManager.findAttackableAreasInBattleStage(player).stream().findAny().get().getId();
	}

	@XmlAttribute(name = "ROUND")
	public int getRound() {
		return this.round;
	}

	@XmlAttribute(name = "SCREEN")
	public Screen getScreen() {
		return this.screen;
	}

	@XmlAttribute(name = "SEL")
	public String getSel() {
		final StringBuilder builder = new StringBuilder(36);
		for (int i = 0; i < Game.GAME_PLAYERS_COUNT; i++)
			builder.append(String.format("%02X", this.selections.get(i + 1).get()));
		return builder.toString();
	}

	@XmlAttribute(name = "STATE")
	public int getState() {
		return this.getCurrentFrame().getState();
	}

	@XmlAttribute(name = "WO")
	public String getWarOrder() {
		return StringUtils.join(this.warOrder, "");
	}

	public boolean isBattle() {
		return this.tipRound > MAP_BG_TIP_ROUNDS;
	}

	public boolean isDeffenderDestroyed() {
		return this.points.get(this.defender).get() == 0;
	}

	public boolean isFinished() {
		return this.isGameFinished;
	}

	private boolean isOffenderDestroyed() {
		final Integer battleOffender = this.getBattleOffender(this.round, this.leg);
		return this.points.get(battleOffender).get() == 0;
	}

	public boolean isPlayerSelectArea(final int playerId) {
		return this.selections.get(playerId).get() != 0;
	}

	public boolean isSelectedAreaTower() {
		return this.areasManager.get(this.selectedArea).getType().equals(AreaType.CASTLE);
	}

	private void nextLeg() {
		this.leg = this.leg % 3 + 1;
		LOG.debug("leg: " + this.leg);
		LOG.debug("round: " + this.round);
		if (this.leg == 1) {
			this.round++;
			LOG.debug("round: " + this.round);
			if (this.round == 4) {
				final List<Integer> subList = this.warOrder.subList(0, 9);
				final List<Integer> warOrder = new ArrayList<>();
				warOrder.addAll(subList);
				warOrder.addAll(Arrays.asList(this.getCurrentRanking()));
				this.warOrder = warOrder;
				LOG.debug("warorder changed to: " + this.warOrder);
			}
			if (this.round > 4 || this.players == 1) {
				this.isGameFinished = true;
				this.leg = 1;
				this.offender = 1;
				this.round = 4;
			}
		}
	}

	public void nextTipRound() {
		this.tipRound++;
		this.round++;
	}

	public void occupyAttackedArea() {
		final Area current = this.areasManager.get(this.selectedArea);
		switch (current.getState()) {
		case DEFAULT_AREA_STATE: {
			current.setCode(OCCUPIED_AREA_STATE * 10 + this.offender);
			current.setValue(OCCUPIED_AREA_VALUE);
			final int updatedOffenderPoints = this.points.get(this.offender).get() + OCCUPIED_AREA_VALUE;
			final int updatedDeffenderPoints = this.points.get(this.defender).get() - DEFAULT_AREA_VALUE;
			this.points.get(this.offender).set(updatedOffenderPoints);
			this.points.get(this.defender).set(updatedDeffenderPoints);
			break;
		}
		case OCCUPIED_AREA_STATE: {
			current.setCode(OCCUPIED_AREA_STATE * 10 + this.offender);
			current.setValue(OCCUPIED_AREA_VALUE);
			final int updatedOffenderPoints = this.points.get(this.offender).get() + OCCUPIED_AREA_VALUE;
			final int updatedDeffenderPoints = this.points.get(this.defender).get() - OCCUPIED_AREA_VALUE;
			this.points.get(this.offender).set(updatedOffenderPoints);
			this.points.get(this.defender).set(updatedDeffenderPoints);
			break;
		}
		case CASTLE_STATE: {
			current.setCode(OCCUPIED_AREA_STATE * 10 + this.offender);
			current.setValue(OCCUPIED_AREA_VALUE);
			final int updatedOffenderPoints = this.points.get(this.offender).get() + CASTLE_VALUE;
			final int updatedDeffenderPoints = this.points.get(this.defender).get() - CASTLE_VALUE;
			this.points.get(this.offender).set(updatedOffenderPoints);
			this.points.get(this.defender).set(updatedDeffenderPoints);
			break;
		}
		default:
			break;
		}
	}

	public void occupyEmptyArea(final int me, final int areaId) {
		this.selectedArea = areaId;
		final Area current = this.areasManager.get(areaId);
		current.setCode(DEFAULT_AREA_STATE * 10 + me);
		current.setValue(DEFAULT_AREA_VALUE);
		current.setType(AreaType.TERITORY);
		this.selections.get(me).set(areaId);
		final int updatedPoints = this.points.get(me).get() + DEFAULT_AREA_VALUE;
		this.points.get(me).set(updatedPoints);
	}

	public void prepareNextLeg() {
		this.nextLeg();
		while (!this.isGameFinished && this.isOffenderDestroyed())
			this.nextLeg();
	}

	public void setBattle(final int battle) {
		this.round = battle;
	}

	public void setCurrentFrame(final GameFrame currentFrame) {
		LOG.debug("current frame {}", this.currentFrame);
		this.currentFrame = currentFrame;
	}

	public void setOffender(final int offender) {
		this.offender = offender;
	}

	public void switchBattleOffender() {
		this.offender = this.getBattleOffender(this.round, this.leg);
	}

	@Override
	public String toString() {
		return "GameState [areas=" + this.areasManager + ", bases=" + this.bases + ", bpts=" + this.bpts
				+ ", selectedArea=" + this.selectedArea + ", currentFrame=" + this.currentFrame + ", defender="
				+ this.defender + ", humanConnected=" + this.humanConnected + ", isGameFinished=" + this.isGameFinished
				+ ", leg=" + this.leg + ", offender=" + this.offender + ", points=" + this.points + ", round="
				+ this.round + ", screen=" + this.screen + ", sel=" + this.selections + ", tipRound=" + this.tipRound
				+ ", warOrder=" + this.warOrder + "]";
	}
}
