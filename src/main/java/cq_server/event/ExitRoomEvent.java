package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "EXITROOM")
@XmlAccessorType(XmlAccessType.FIELD)
public final class ExitRoomEvent extends BaseEvent {
	public ExitRoomEvent() {
		super(EventType.EXITROOM);
	}
}
