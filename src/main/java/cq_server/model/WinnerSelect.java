package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "WINNERSELECT")
public class WinnerSelect {
	@XmlAttribute(name = "PLAYER")
	private int playerId;

	@XmlAttribute(name = "AREA")
	private int area;

	public WinnerSelect() {
		// TODO Auto-generated constructor stub
	}

	public WinnerSelect(int player, int area) {
		super();
		this.playerId = player;
		this.area = area;
	}
}