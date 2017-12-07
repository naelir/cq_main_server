package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "LOGOUT")
@XmlAccessorType(XmlAccessType.FIELD)
public final class LogoutEvent extends BaseEvent {
	public LogoutEvent() {
		super(EventType.LOGOUT);
	}
}
