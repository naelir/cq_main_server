package cq_server.model;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.game.BasePlayer;
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

	private final Integer id;

	private final List<String> invited;

	private final CountryMap map;

	private final List<String> names;

	private final Integer oopp;

	private final Integer personal;

	private final Set<BasePlayer> players;

	private final RoomSettings settings;

	protected SepRoom() {
		this(null, null);
	}

	public SepRoom(final BasePlayer creator, final RoomSettings settings) {
		this.id = creator.getId();
		this.players = new ConcurrentSkipListSet<>();
		this.players.add(creator);
		this.names = new CopyOnWriteArrayList<>();
		this.names.add(creator.getName());
		this.invited = new CopyOnWriteArrayList<>();
		this.invited.add(creator.getName());
		this.map = CountryMap.BG;
		this.oopp = settings.getOopp().getValue();
		this.personal = 1;
		this.settings = settings;
		if (OOPP.HASROBOT.equals(settings.getOopp())) {
			this.names.add("robot-1");
			this.names.add("robot-2");
		}
	}

	public void add(final BasePlayer player) {
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

	public Set<BasePlayer> getPlayers() {
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

	public boolean remove(final BasePlayer player) {
		return this.players.remove(player);
	}
 
}
