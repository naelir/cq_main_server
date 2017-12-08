package cq_server.event;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "CHATMSG")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class ChatMessageEvent extends BaseEvent {
	private String msg;

	public ChatMessageEvent() {
		super(EventType.CHATMESSAGE);
	}

	public ChatMessageEvent(final String msg) {
		super(EventType.CHATMESSAGE);
		this.msg = msg;
	}

	@XmlAttribute(name = "MSG")
	@NotNull
	public String getMsg() {
		return this.msg;
	}

	public void setMsg(final String msg) {
		this.msg = msg;
	}
}
