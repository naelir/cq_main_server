package cq_server.game;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import cq_server.model.Area;
import cq_server.model.AreaType;

public class AreasManager {
	private final Map<Integer, Area> areas;

	private final Map<Integer, List<Integer>> neighbors;

	private final Random random;

	public AreasManager(final Map<Integer, List<Integer>> neighbors) {
		this.neighbors = neighbors;
		this.areas = new ConcurrentHashMap<>(neighbors.size());
		this.random = new Random();
		for (int i = 0; i < neighbors.size(); i++)
			this.areas.put(i + 1, new Area(i + 1, AreaType.EMPTY, 0, 0));
	}

	private Set<Area> allEmptyAreas() {
		final Set<Area> emptyAreas = new HashSet<>();
		for (final Area area : this.areas.values())
			if (area.getType().equals(AreaType.EMPTY))
				emptyAreas.add(area);
		return emptyAreas;
	}

	public Set<Area> findAttackableAreasInBattleStage(final int playerId) {
		final Set<Area> myAreas = this.personalAreas(playerId);
		final Set<Area> myNeighbors = this.neighborAreas(myAreas);
		final Set<Area> all = this.areas.values().stream().collect(Collectors.toSet());
		all.removeAll(myAreas);
		all.retainAll(myNeighbors);
		return all;
	}

	public Set<Area> findAttackableAreasInFinalStage(final int playerId) {
		final Set<Area> myAreas = this.personalAreas(playerId);
		final Set<Area> allAreas = this.areas.values().stream().collect(Collectors.toSet());
		allAreas.removeAll(myAreas);
		return allAreas;
	}

	public Area findBase() {
		final List<Area> freeAreas = this.findEmptyAreasForTower().stream().collect(Collectors.toList());
		final int size = freeAreas.size();
		return freeAreas.get(this.random.nextInt(size));
	}

	private Set<Area> findEmptyAreasForTower() {
		final Set<Area> notEmpty = this.occupiedAreas();
		final Set<Area> neighborsOfNotEmpty = this.neighborAreas(notEmpty);
		final Set<Area> allAreas = this.areas.values().stream().collect(Collectors.toSet());
		allAreas.removeAll(notEmpty);
		allAreas.removeAll(neighborsOfNotEmpty);
		return allAreas;
	}

	public Set<Area> findSelectableEmptyNeighborsInTipStage(final Integer playerId) {
		final Set<Area> myAreas = this.personalAreas(playerId);
		final Set<Area> neighbors = this.neighborAreas(myAreas);
		final Set<Area> notEmpty = this.occupiedAreas();
		final Set<Area> emptyAreas = this.allEmptyAreas();
		final Set<Area> allAreas = this.areas.values().stream().collect(Collectors.toSet());
		allAreas.removeAll(myAreas);
		allAreas.removeAll(notEmpty);// remains all emtpy areas
		allAreas.retainAll(neighbors);// some neighbor, which is empty
		if (allAreas.size() == 0)
			// there is no empty neighbor areas, choose some area from other
			// remaining empties
			return emptyAreas;
		// System.out.println("empty neighbors " + allAreas);
		return allAreas;
	}

	public Area get(final int id) {
		return this.areas.get(id);
	}

	private Set<Area> neighborAreas(final Set<Area> areas) {
		final Set<Area> allNeighbors = new HashSet<>();
		for (final Area area : areas) {
			final List<Integer> neighbors = this.neighbors.get(area.getId());
			for (final Integer n : neighbors)
				allNeighbors.add(this.areas.get(n));
		}
		return allNeighbors;
	}

	private Set<Area> occupiedAreas() {
		final Set<Area> notEmpty = new HashSet<>();
		for (final Area area : this.areas.values())
			if (!area.getType().equals(AreaType.EMPTY))
				notEmpty.add(area);
		return notEmpty;
	}

	private Set<Area> personalAreas(final int playerId) {
		final Set<Area> areasNums = new HashSet<>();
		for (final Area area : this.areas.values())
			if (area.getCode() % 10 == playerId)
				areasNums.add(area);
		return areasNums;
	}

	public int size() {
		return this.areas.size();
	}

	public void switchOwner(final int from, final int to) {
		for (final Area area : this.areas.values())
			if (area.getCode() % 10 == from) {
				final int code = area.getCode() - from + to;
				area.setCode(code);
				if (area.getType().equals(AreaType.CASTLE))
					area.setType(AreaType.TERITORY);
			}
	}
}
