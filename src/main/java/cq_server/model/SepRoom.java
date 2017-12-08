package cq_server.model;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.game.Game;

/*
 * <SEPROOM ID="4687388" MAP="FR" U1="SayaClau" U2="" U3="" DEPU1="SayaClau"
 * PERSONAL="0" OOPP="1" RULES="2" QCATS="15" />
 */
@XmlRootElement(name = "SEPROOM")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class SepRoom {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SepRoom.class);

	private Integer id;

	private List<String> invited;

	private CountryMap map;

	private List<String> names;

	private Integer oopp;

	private Integer personal;

	private Set<Player> players;

	private RoomSettings settings;

	protected SepRoom() {
	}

	public SepRoom(
			final int id,
			final List<Player> players,
			final List<String> names,
			final RoomSettings settings) {
		this.id = id;
		this.players = new CopyOnWriteArraySet<>(players);
		this.names = new CopyOnWriteArrayList<>(names);
		this.invited = new CopyOnWriteArrayList<>(names);
		this.map = CountryMap.BG;
		this.oopp = settings.getOopp().getValue();
		this.personal = 1;
		this.settings = settings;
	}

	public void add(final Player player) {
		if (this.players.size() >= Game.GAME_PLAYERS_COUNT) {
		} else {
			this.players.add(player);
			this.names.add(player.getName());
		}
	}

	public void deny() {
		this.players.stream().forEach(element -> element.getWaitState().setSepRoomSel(0));
	}

	@XmlAttribute(name = "DEPU1")
	public String getDepu1() {
		if (this.players.size() >= 1)
			return this.names.get(0);
		return null;
	}

	@XmlAttribute(name = "ID")
	public int getId() {
		return this.id;
	}

	public List<String> getInvited() {
		return this.invited;
	}

	@XmlAttribute(name = "MAP")
	public CountryMap getMap() {
		return this.map;
	}

	@XmlAttribute(name = "OOPP")
	public Integer getOopp() {
		return this.oopp;
	}

	@XmlAttribute(name = "PERSONAL")
	public Integer getPersonal() {
		return this.personal;
	}

	public Set<Player> getPlayers() {
		return this.players;
	}

	@XmlAttribute(name = "RULES")
	public Integer getRules() {
		return this.settings.getRules().getValue();
	}

	public RoomSettings getSettings() {
		return this.settings;
	}

	@XmlAttribute(name = "U1")
	public String getU1() {
		if (this.players.size() >= 1)
			return this.names.get(0);
		return null;
	}

	@XmlAttribute(name = "U2")
	public String getU2() {
		if (this.players.size() >= 2)
			return this.names.get(1);
		return null;
	}

	@XmlAttribute(name = "U3")
	public String getU3() {
		if (this.players.size() >= 3)
			return this.names.get(2);
		return null;
	}

	public boolean isFull() {
		return OOPP.HASROBOT.equals(this.settings.getOopp()) || this.players.size() == Game.GAME_PLAYERS_COUNT;
	}

	public boolean remove(final Player player) {
		return this.players.remove(player);
	}
}
