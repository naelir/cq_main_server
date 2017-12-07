package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "CONNSTATUS")
public class ConnStatus {
	@XmlAttribute(name = "GAMEID")
	private Integer gameId;

	private Page state;

	@XmlAttribute(name = "YOU")
	private String you;

	@XmlAttribute(name = "PLAYER1")
	private String pl1;

	@XmlAttribute(name = "PLAYER2")
	private String pl2;

	@XmlAttribute(name = "PLAYER3")
	private String pl3;

	public ConnStatus() {
		this.state = Page.WAITHALL;
		this.you = "";
	}

	public ConnStatus(Integer gameId) {
		this.state = Page.GAME;
		this.gameId = gameId;
		this.you = "LR";
		this.pl1 = "CLR";
		this.pl2 = "CLR";
		this.pl3 = "CLR";
	}
}