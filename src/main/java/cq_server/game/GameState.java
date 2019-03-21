package cq_server.game;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.game.Game.Stage;
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

	private final Map<Integer, Area> areas;

	private final Map<Integer, List<Integer>> neighbors;

	private final Random random;

	public GameState() {
		this(new Builder().empty());
	}

	public GameState(final Builder builder) {
		this.bases = builder.bases;
		this.bpts = builder.bpts;
		this.currentFrame = builder.currentFrame;
		this.defender = builder.defender;
		this.humanConnected = builder.humanConnected;
		this.isGameFinished = builder.isGameFinished;
		this.leg = builder.leg;
		this.offender = builder.offender;
		this.players = builder.players;
		this.points = builder.points;
		this.round = builder.round;
		this.screen = builder.screen;
		this.selections = builder.selections;
		this.selectedArea = builder.selectedArea;
		this.tipRound = builder.tipRound;
		this.warOrder = builder.warOrder;
		this.areas = builder.areas;
		this.neighbors = builder.neighbors;
		this.random = builder.random;
	}

	public void addBase(final Integer player) {
		final Area area = this.findBase();
		area.setCode(CASTLE_STATE * 10 + player);
		area.setType(AreaType.CASTLE);
		area.setValue(CASTLE_VALUE);
		final int areaId = area.getId();
		this.bases.get(player)
			.set(areaId);
		final int updatedPoints = this.points.get(player)
			.get() + CASTLE_VALUE;
		this.points.get(player)
			.set(updatedPoints);
		this.selections.get(player)
			.set(areaId);
	}

	private Set<Area> allEmptyAreas() {
		final Set<Area> emptyAreas = new HashSet<>();
		for (final Area area : this.areas.values())
			if (area.getType()
				.equals(AreaType.EMPTY))
				emptyAreas.add(area);
		return emptyAreas;
	}

	public void attackArea(final int me, final int areaId) {
		this.selectedArea = areaId;
		this.selections.get(me)
			.set(areaId);
		this.defender = this.getOwner(areaId);
		this.offender = me;
	}

	public void battleStage() {
		if (this.warOrder == null)
			this.warOrder = DEFAULT_WARORDER;
	}

	public void clearSelections() {
		this.selections.values()
			.stream()
			.forEach(element -> element.set(0));
	}

	public void decreaseActivePlayers() {
		this.players--;
	}

	public void deffendArea() {
		final int pointsUpdated = this.points.get(this.defender)
			.get() + DEFEND_AREA_AWARD_POINTS;
		this.points.get(this.defender)
			.set(pointsUpdated);
	}

	public void destroyTower() {
		final int baseUpdated = this.bases.get(this.defender)
			.get() + MINUS_TOWER_BASE_VALUE_CORRECTION;
		this.bases.get(this.defender)
			.set(baseUpdated);
		// if 3 towers destroyed, all areas and points moves to offender
		if (baseUpdated > TOWER_DESTROYED_VALUE) {
			this.switchOwner(this.defender, this.offender);
			final int allPoints = this.points.get(this.offender)
				.get()
					+ this.points.get(this.defender)
						.get();
			this.points.get(this.offender)
				.set(allPoints);
			this.points.get(this.defender)
				.set(0);
		}
	}

	public void disconnect(final Integer playerId) {
		this.humanConnected.remove(playerId);
	}

	public Set<Area> findAreas(final int playerId, final Stage stage) {
		switch (stage) {
		case SPREADING: {
			final Set<Area> myAreas = this.personalAreas(playerId);
			final Set<Area> neighbors = this.neighborAreas(myAreas);
			final Set<Area> notEmpty = this.occupiedAreas();
			final Set<Area> emptyAreas = this.allEmptyAreas();
			final Set<Area> all = this.areas.values()
				.stream()
				.collect(Collectors.toSet());
			all.removeAll(myAreas);
			all.removeAll(notEmpty);// remains all emtpy areas
			all.retainAll(neighbors);// some neighbor, which is empty
			if (all.size() == 0)
				// there is no empty neighbor areas, choose some area from other
				// remaining empties
				return emptyAreas;
			return all;
		}
		case BATTLE: {
			final Set<Area> myAreas = this.personalAreas(playerId);
			final Set<Area> myNeighbors = this.neighborAreas(myAreas);
			final Set<Area> all = this.areas.values()
				.stream()
				.collect(Collectors.toSet());
			all.removeAll(myAreas);
			all.retainAll(myNeighbors);
			return all;
		}
		case LAST_ROUND: {
			final Set<Area> myAreas = this.personalAreas(playerId);
			final Set<Area> all = this.areas.values()
				.stream()
				.collect(Collectors.toSet());
			all.removeAll(myAreas);
			return all;
		}
		default:
			return Collections.emptySet();
		}
	}

	public Area findBase() {
		final List<Area> freeAreas = this.findEmptyAreasForTower()
			.stream()
			.collect(Collectors.toList());
		final int size = freeAreas.size();
		return freeAreas.get(this.random.nextInt(size));
	}

	private Set<Area> findEmptyAreasForTower() {
		final Set<Area> notEmpty = this.occupiedAreas();
		final Set<Area> neighborsOfNotEmpty = this.neighborAreas(notEmpty);
		final Set<Area> allAreas = this.areas.values()
			.stream()
			.collect(Collectors.toSet());
		allAreas.removeAll(notEmpty);
		allAreas.removeAll(neighborsOfNotEmpty);
		return allAreas;
	}

	public Area get(final int areaId) {
		return this.areas.get(areaId);
	}

	public Map<Integer, Integer> getAreaCodes() {
		final Map<Integer, Integer> codes = new HashMap<>(3);
		for (int i = 0; i < this.areas.size(); i++)
			codes.put(i + 1, this.areas.get(i + 1)
				.getCode());
		return codes;
	}

	@XmlAttribute(name = "AREAS")
	public String getAreas() {
		final StringBuilder builder = new StringBuilder();
		final Map<Integer, Integer> codes = this.getAreaCodes();
		for (int i = 0; i < codes.size(); i++)
			builder.append(String.format("%02d", codes.get(i + 1)));
		return builder.toString();
	}

	//@formatter:off
	public Set<Integer> getAvailableAreas(final int player) {
		if (this.tipRound <= MAP_BG_TIP_ROUNDS)
			return this
					.findAreas(player, Game.Stage.SPREADING)
					.stream()
					.map(element -> element.getId())
					.collect(Collectors.toSet());
		else if (this.round < MAP_BG_BATTLE_ROUNDS)
			return this
					.findAreas(player, Game.Stage.BATTLE)
					.stream()
					.map(element -> element.getId())
					.collect(Collectors.toSet());
		else
			return this
					.findAreas(player, Game.Stage.LAST_ROUND)
					.stream()
					.map(element -> element.getId())
					.collect(Collectors.toSet());
	}
	//@formatter:on

	@XmlAttribute(name = "BASES")
	public String getBases() {
		final StringBuilder builder = new StringBuilder(36);
		for (int i = 0; i < this.bases.size(); i++)
			builder.append(String.format("%02X", this.bases.get(i + 1)
				.get()));
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

	public Integer[] getCurrentRanking() {
		final Integer[] ranking = this.points.keySet()
			.toArray(new Integer[0]);
		final Comparator<Integer> cmp = (o1, o2) -> Integer.compare(this.points.get(o1)
			.get(),
				this.points.get(o2)
					.get());
		final Comparator<Integer> reverseOrder = Collections.reverseOrder(cmp);
		Arrays.sort(ranking, reverseOrder);
		return ranking;
	}

	public int getDeffender() {
		return this.defender;
	}

	@XmlAttribute(name = "HC")
	public String getHumanConnected() {
		return StringUtils.join(this.humanConnected, "");
	}

	@XmlTransient
	public int getOffender() {
		return this.offender;
	}

	public int getOwner(final int areaId) {
		return this.areas.get(areaId)
			.getCode() % 10;
	}

	@XmlAttribute(name = "PHASE")
	public int getPhase() {
		return this.getCurrentFrame()
			.getPhase();
	}

	@XmlAttribute(name = "POINTS")
	public String getPoints() {
		if (this.points.isEmpty()) {
			return null;
		} else {
			return String.format("%d,%d,%d", this.points.get(1)
				.get(),
					this.points.get(2)
						.get(),
					this.points.get(3)
						.get());
		}
	}

	public int getRandomArea(final int player) {
		int area = 0;
		if (this.tipRound <= MAP_BG_TIP_ROUNDS)
			area = this.findAreas(player, Stage.SPREADING)
				.stream()
				.findAny()
				.get()
				.getId();
		else
			area = this.findAreas(player, Stage.BATTLE)
				.stream()
				.findAny()
				.get()
				.getId();
		return area;
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
		for (int i = 0; i < this.selections.size(); i++)
			builder.append(String.format("%02X", this.selections.get(i + 1)
				.get()));
		return builder.toString();
	}

	@XmlAttribute(name = "STATE")
	public int getState() {
		return this.getCurrentFrame()
			.getState();
	}

	@XmlAttribute(name = "WO")
	public String getWarOrder() {
		return StringUtils.join(this.warOrder, "");
	}

	public boolean isBattle() {
		return this.tipRound > MAP_BG_TIP_ROUNDS;
	}

	public boolean isDeffenderDestroyed() {
		return this.points.get(this.defender)
			.get() == 0;
	}

	public boolean isFinished() {
		return this.isGameFinished;
	}

	private boolean isOffenderDestroyed() {
		final Integer battleOffender = this.getBattleOffender(this.round, this.leg);
		return this.points.get(battleOffender)
			.get() == 0;
	}

	public boolean isPlayerSelectArea(final int playerId) {
		return this.selections.get(playerId)
			.get() != 0;
	}

	public boolean isSelectedAreaTower() {
		return this.isTower(this.selectedArea);
	}

	public boolean isTower(final int areaId) {
		return this.areas.get(areaId)
			.getType()
			.equals(AreaType.CASTLE);
	}

	private Set<Area> neighborAreas(final Set<Area> areas) {
		final Set<Area> allNeighbors = new HashSet<>();
		for (final Area area : areas) {
			final List<Integer> neighbors = this.neighbors.get(area.getId());
			for (final Integer neighbor : neighbors)
				allNeighbors.add(this.areas.get(neighbor));
		}
		return allNeighbors;
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

	private Set<Area> occupiedAreas() {
		final Set<Area> occupied = new HashSet<>();
		for (final Area area : this.areas.values())
			if (!area.getType()
				.equals(AreaType.EMPTY))
				occupied.add(area);
		return occupied;
	}

	public void occupyAttackedArea() {
		final Area current = this.areas.get(this.selectedArea);
		switch (current.getState()) {
		case DEFAULT_AREA_STATE: {
			current.setCode(OCCUPIED_AREA_STATE * 10 + this.offender);
			current.setValue(OCCUPIED_AREA_VALUE);
			final int updatedOffenderPoints = this.points.get(this.offender)
				.get() + OCCUPIED_AREA_VALUE;
			final int updatedDeffenderPoints = this.points.get(this.defender)
				.get() - DEFAULT_AREA_VALUE;
			this.points.get(this.offender)
				.set(updatedOffenderPoints);
			this.points.get(this.defender)
				.set(updatedDeffenderPoints);
			break;
		}
		case OCCUPIED_AREA_STATE: {
			current.setCode(OCCUPIED_AREA_STATE * 10 + this.offender);
			current.setValue(OCCUPIED_AREA_VALUE);
			final int updatedOffenderPoints = this.points.get(this.offender)
				.get() + OCCUPIED_AREA_VALUE;
			final int updatedDeffenderPoints = this.points.get(this.defender)
				.get() - OCCUPIED_AREA_VALUE;
			this.points.get(this.offender)
				.set(updatedOffenderPoints);
			this.points.get(this.defender)
				.set(updatedDeffenderPoints);
			break;
		}
		case CASTLE_STATE: {
			current.setCode(OCCUPIED_AREA_STATE * 10 + this.offender);
			current.setValue(OCCUPIED_AREA_VALUE);
			final int updatedOffenderPoints = this.points.get(this.offender)
				.get() + CASTLE_VALUE;
			final int updatedDeffenderPoints = this.points.get(this.defender)
				.get() - CASTLE_VALUE;
			this.points.get(this.offender)
				.set(updatedOffenderPoints);
			this.points.get(this.defender)
				.set(updatedDeffenderPoints);
			break;
		}
		default:
			break;
		}
	}

	public void occupyEmptyArea(final int me, final int areaId) {
		this.selectedArea = areaId;
		final Area current = this.get(areaId);
		current.setCode(DEFAULT_AREA_STATE * 10 + me);
		current.setValue(DEFAULT_AREA_VALUE);
		current.setType(AreaType.TERITORY);
		this.selections.get(me)
			.set(areaId);
		final int updatedPoints = this.points.get(me)
			.get() + DEFAULT_AREA_VALUE;
		this.points.get(me)
			.set(updatedPoints);
	}

	private Set<Area> personalAreas(final int playerId) {
		final Set<Area> personal = new HashSet<>();
		for (final Area area : this.areas.values())
			if (area.getCode() % 10 == playerId)
				personal.add(area);
		return personal;
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
		LOG.debug("set current frame to {}", currentFrame);
		this.currentFrame = currentFrame;
	}

	public void setOffender(final int offender) {
		this.offender = offender;
	}

	public void switchBattleOffender() {
		this.offender = this.getBattleOffender(this.round, this.leg);
	}

	public void switchOwner(final int from, final int to) {
		for (final Area area : this.areas.values())
			if (area.getCode() % 10 == from) {
				final int code = area.getCode() - from + to;
				area.setCode(code);
				if (area.getType()
					.equals(AreaType.CASTLE))
					area.setType(AreaType.TERITORY);
			}
	}

	@Override
	public String toString() {
		return "GameState [areas=" + this.areas + ", bases=" + this.bases + ", bpts=" + this.bpts + ", selectedArea="
				+ this.selectedArea + ", currentFrame=" + this.currentFrame + ", defender=" + this.defender
				+ ", humanConnected=" + this.humanConnected + ", isGameFinished=" + this.isGameFinished + ", leg="
				+ this.leg + ", offender=" + this.offender + ", points=" + this.points + ", round=" + this.round
				+ ", screen=" + this.screen + ", sel=" + this.selections + ", tipRound=" + this.tipRound + ", warOrder="
				+ this.warOrder + "]";
	}

	public static final class Builder {
		private Map<Integer, AtomicInteger> bases;

		private Map<Integer, AtomicInteger> bpts;

		private GameFrame currentFrame;

		private int defender;

		private List<Integer> humanConnected;

		private boolean isGameFinished;

		private int leg;

		private int offender;

		private int players;

		private Map<Integer, AtomicInteger> points;

		private int round;

		private Screen screen;

		private Map<Integer, AtomicInteger> selections;

		private int selectedArea;

		private int tipRound;

		private List<Integer> warOrder;

		private Map<Integer, Area> areas;

		private Map<Integer, List<Integer>> neighbors;

		private Random random;

		public Builder() {
		}

		public GameState build() {
			return new GameState(this);
		}

		public Builder empty() {
			this.areas = Collections.emptyMap();
			this.bases = Collections.emptyMap();
			this.bpts = Collections.emptyMap();
			this.currentFrame = GameFrame.START;
			this.defender = 0;
			this.humanConnected = Collections.emptyList();
			this.isGameFinished = false;
			this.leg = 0;
			this.neighbors = Collections.emptyMap();
			this.offender = 0;
			this.players = 0;
			this.points = Collections.emptyMap();
			this.random = new Random();
			this.round = 0;
			this.screen = Screen.MAP_BG;
			this.selectedArea = 0;
			this.selections = Collections.emptyMap();
			this.tipRound = 0;
			this.warOrder = Collections.emptyList();
			return this;
		}

		public Builder setAreas(final Map<Integer, Area> areas) {
			this.areas = areas;
			return this;
		}

		public Builder setBases(final Map<Integer, AtomicInteger> bases) {
			this.bases = bases;
			return this;
		}

		public Builder setBpts(final Map<Integer, AtomicInteger> bpts) {
			this.bpts = bpts;
			return this;
		}

		public Builder setCurrentFrame(final GameFrame currentFrame) {
			this.currentFrame = currentFrame;
			return this;
		}

		public Builder setDefender(final int defender) {
			this.defender = defender;
			return this;
		}

		public Builder setHumanConnected(final List<Integer> humanConnected) {
			this.humanConnected = humanConnected;
			return this;
		}

		public Builder setIsGameFinished(final boolean isGameFinished) {
			this.isGameFinished = isGameFinished;
			return this;
		}

		public Builder setLeg(final int leg) {
			this.leg = leg;
			return this;
		}

		public Builder setNeighbors(final Map<Integer, List<Integer>> neighbors) {
			this.neighbors = neighbors;
			return this;
		}

		public Builder setOffender(final int offender) {
			this.offender = offender;
			return this;
		}

		public Builder setPlayers(final int players) {
			this.players = players;
			return this;
		}

		public Builder setPoints(final Map<Integer, AtomicInteger> points) {
			this.points = points;
			return this;
		}

		public Builder setRandom(final Random random) {
			this.random = random;
			return this;
		}

		public Builder setRound(final int round) {
			this.round = round;
			return this;
		}

		public Builder setScreen(final Screen screen) {
			this.screen = screen;
			return this;
		}

		public Builder setSelectedArea(final int selectedArea) {
			this.selectedArea = selectedArea;
			return this;
		}

		public Builder setSelections(final Map<Integer, AtomicInteger> selections) {
			this.selections = selections;
			return this;
		}

		public Builder setTipRound(final int tipRound) {
			this.tipRound = tipRound;
			return this;
		}

		public Builder setWarOrder(final List<Integer> warOrder) {
			this.warOrder = warOrder;
			return this;
		}
	}
}
