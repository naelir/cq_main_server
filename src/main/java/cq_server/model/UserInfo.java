package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "USERINFO")
public class UserInfo {
	@XmlAttribute(name = "CLP")
	private int clp;

	@XmlAttribute(name = "DAYRANK")
	private int dayrank;

	@XmlAttribute(name = "GAMECOUNT")
	private int gamecount;

	@XmlAttribute(name = "ID")
	private int id;

	@XmlAttribute(name = "JEP")
	private int jep;

	@XmlAttribute(name = "NAME")
	private String name;

	@XmlAttribute(name = "NOCHAT")
	private int nochat;

	@XmlAttribute(name = "ONLINE")
	private int online;

	public UserInfo() {
		this(null);
	}

	public UserInfo(String name) {
		this.name = name;
		this.online = 1;
		this.nochat = 0;
		this.jep = 10000;
		this.gamecount = 10000;
		this.dayrank = 100;
		this.clp = 100;
	}
}