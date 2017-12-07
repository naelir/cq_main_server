package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "MESSAGE")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class MessageEvent extends BaseEvent {
	private String to;

	private String message;

	public MessageEvent() {
		super(EventType.MESSAGE);
	}

	public MessageEvent(final String to, final String message) {
		super(EventType.MESSAGE);
		this.to = to;
		this.message = message;
	}

	@XmlAttribute(name = "TEXT")
	public String getMessage() {
		return this.message;
	}

	@XmlAttribute(name = "TO")
	public String getTo() {
		return this.to;
	}

	public void setTo(final String to) {
		this.to = to;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
