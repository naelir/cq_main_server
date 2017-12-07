package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MESSAGE")
public final class Message {
	@XmlAttribute(name = "MSG")
	private final String msg;

	@XmlAttribute(name = "FROM")
	private final int from;

	@XmlAttribute(name = "TO")
	private final int to;

	@XmlAttribute(name = "COLOR")
	private final int color;

	public Message() {
		this(0, 0, null);
	}

	public Message(int from, int to, String msg) {
		super();
		this.from = from;
		this.to = to;
		this.msg = msg;
		this.color = 0;
	}

	public String getMsg() {
		return msg;
	}

	public Integer getFrom() {
		return from;
	}

	public int getColor() {
		return color;
	}

	public Integer getTo() {
		return to;
	}
}
