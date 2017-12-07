package cq_server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CHATMSG")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class ChatMsg {
	private int id;

	private String sender;

	private String msg;

	private int color;

	public ChatMsg() {
	}

	public ChatMsg(final String sender, final String msg) {
		super();
		this.id = 0;
		this.sender = sender;
		this.msg = msg;
		this.color = 1;
	}

	@XmlAttribute(name = "COLOR")
	public int getColor() {
		return this.color;
	}

	@XmlAttribute(name = "MID")
	public int getId() {
		return this.id;
	}

	@XmlAttribute(name = "MSG")
	public String getMsg() {
		return this.msg;
	}

	@XmlAttribute(name = "SENDER")
	public String getSender() {
		return this.sender;
	}

	public void setId(final int id) {
		this.id = id;
	}
}
