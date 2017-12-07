package cq_server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CMD")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class CmdTip {
	private final CmdType type;

	private final int players;

	public CmdTip() {
		this(0);
	}

	public CmdTip(final int players) {
		this.type = CmdType.TIP;
		this.players = players;
	}

	@XmlAttribute(name = "PLAYERS")
	public int getPlayers() {
		return this.players;
	}

	@XmlAttribute(name = "CMD")
	public CmdType getType() {
		return this.type;
	}
}