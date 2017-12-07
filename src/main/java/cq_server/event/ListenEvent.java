package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "LISTEN")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class ListenEvent extends BaseEvent {
	private Boolean ready;

	public ListenEvent() {
		super(EventType.LISTEN);
	}

	public ListenEvent(final Boolean ready) {
		super(EventType.LISTEN);
		this.ready = ready;
	}

	@XmlAttribute(name = "READY")
	public Boolean getReady() {
		return this.ready;
	}

	public void setReady(final Boolean ready) {
		this.ready = ready;
	}

}