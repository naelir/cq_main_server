package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "ENTERROOM")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class EnterRoomEvent extends BaseEvent {
	private Integer sepRoomId;

	private Integer roomId;

	public EnterRoomEvent() {
		super(EventType.ENTERROOM);
	}

	public EnterRoomEvent(final Integer sepRoomId, final Integer roomId) {
		super(EventType.ENTERROOM);
		this.sepRoomId = sepRoomId;
		this.roomId = roomId;
	}

	@XmlAttribute(name = "ROOM")
	public Integer getRoomId() {
		return this.roomId;
	}

	@XmlAttribute(name = "SEPROOM")
	public Integer getSepRoomId() {
		return this.sepRoomId;
	}

	public void setSepRoomId(final Integer sepRoomId) {
		this.sepRoomId = sepRoomId;
	}

	public void setRoomId(final Integer roomId) {
		this.roomId = roomId;
	}
}
