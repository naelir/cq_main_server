package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "CHATCOLOR")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ChatColorEvent extends BaseEvent {
	private int color;

	public ChatColorEvent() {
		super(EventType.CHATCOLOR);
	}

	@XmlAttribute(name = "COLOR")
	public int getColor() {
		return this.color;
	}

	public void setColor(final int color) {
		this.color = color;
	}
}
