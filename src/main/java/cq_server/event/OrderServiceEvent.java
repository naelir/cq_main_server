package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;
import cq_server.model.Service;

@XmlRootElement(name = "ORDERSERVICE")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class OrderServiceEvent extends BaseEvent {
	private Service service;

	public OrderServiceEvent() {
		super(EventType.ORDERSERVICE);
	}

	@XmlAttribute(name = "SERVICE")
	public Service getService() {
		return this.service;
	}

	public void setService(final Service service) {
		this.service = service;
	}
}
