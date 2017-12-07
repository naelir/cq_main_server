package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RECONNECT")
public final class Reconnect {
	@XmlAttribute(name = "PLAYER")
	private final int player;

	public Reconnect() {
		this(0);
	}

	public Reconnect(int player) {
		super();
		this.player = player;
	}
}