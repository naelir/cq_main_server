package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "DENYSEPROOM")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class DenySepRoomEvent extends BaseEvent {
	private Integer room;

	public DenySepRoomEvent() {
		super(EventType.DENYSEPROOM);
	}

	public DenySepRoomEvent(final Integer room) {
		super(EventType.DENYSEPROOM);
		this.room = room;
	}

	@XmlAttribute(name = "SEPROOM")
	public Integer getRoom() {
		return this.room;
	}

	public void setRoom(final Integer room) {
		this.room = room;
	}
}