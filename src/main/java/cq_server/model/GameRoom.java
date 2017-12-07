package cq_server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

//<GAMEROOM ID="1" TITLE="Blitzkrieg" MINCLP="0" MAP="FR" TYPE="2" PLAYERS="0" INGAME="7" />
//<GAMEROOM ID="3" TITLE="Room" MINCLP="0" TYPE="5" PLAYERS="1" INGAME="0" />
//<GAMEROOM ID="6" TITLE="Minitournoi" MINCLP="1" MAP="FR"
//TYPE="10" PLAYERS="0" CLOSED="1" NEXTENTERTIME="18:00" REMAINING="68689157" INGAME="0" />
@XmlRootElement(name = "GAMEROOM")
@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class GameRoom {
	private final int id;

	private final String title;

	private final int minClp;

	private final CountryMap map;

	private final GameRoomType type;

	private final int players;

	private final int ingame;

	public GameRoom() {
		this(new Builder().setId(0).setTitle("").setMinClp(0).setType(GameRoomType.ROOM).setMap(CountryMap.BG));
	}

	public GameRoom(final Builder builder) {
		super();
		this.id = builder.id;
		this.title = builder.title;
		this.minClp = builder.minClp;
		this.players = builder.players;
		this.ingame = builder.ingame;
		this.type = builder.type;
		this.map = builder.map;
	}

	@XmlAttribute(name = "ID")
	public int getId() {
		return this.id;
	}

	@XmlAttribute(name = "INGAME")
	public int getIngame() {
		return this.ingame;
	}

	@XmlAttribute(name = "MAP")
	public CountryMap getMap() {
		return this.map;
	}

	@XmlAttribute(name = "MINCLP")
	public int getMinClp() {
		return this.minClp;
	}

	@XmlAttribute(name = "PLAYERS")
	public int getWaitingPlayers() {
		return this.players;
	}

	@XmlAttribute(name = "TITLE")
	public String getTitle() {
		return this.title;
	}

	@XmlAttribute(name = "TYPE")
	public Integer getType() {
		return this.type.getType();
	}

	public static final class Builder {
		private int id;

		private String title;

		private int minClp;

		private CountryMap map;

		private GameRoomType type;

		private int players;

		private int ingame;

		public Builder setId(final int id) {
			this.id = id;
			return this;
		}

		public Builder setIngame(final int ingame) {
			this.ingame = ingame;
			return this;
		}

		public Builder setMap(final CountryMap map) {
			this.map = map;
			return this;
		}

		public Builder setMinClp(final int minClp) {
			this.minClp = minClp;
			return this;
		}

		public Builder setPlayers(final int players) {
			this.players = players;
			return this;
		}

		public Builder setTitle(final String title) {
			this.title = title;
			return this;
		}

		public Builder setType(final GameRoomType type) {
			this.type = type;
			return this;
		}
	}
}
