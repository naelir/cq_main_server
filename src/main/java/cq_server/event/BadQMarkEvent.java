package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "BADQMARK")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BadQMarkEvent extends BaseEvent {
	public BadQMarkEvent() {
		super(EventType.BADQMARK);
	}
}
