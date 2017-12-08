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

	public Message(final int from, final int to, final String msg) {
		super();
		this.from = from;
		this.to = to;
		this.msg = msg;
		this.color = 0;
	}

	public int getColor() {
		return this.color;
	}

	public Integer getFrom() {
		return this.from;
	}

	public String getMsg() {
		return this.msg;
	}

	public Integer getTo() {
		return this.to;
	}
}
