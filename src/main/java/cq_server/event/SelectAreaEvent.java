package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "SELECT")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class SelectAreaEvent extends BaseEvent {
	private Integer area;

	public SelectAreaEvent() {
		super(EventType.SELECT);
	}

	public SelectAreaEvent(final Integer area) {
		super(EventType.SELECT);
		this.area = area;
	}

	@XmlAttribute(name = "AREA")
	public Integer getArea() {
		return this.area;
	}

	public void setArea(final Integer area) {
		this.area = area;
	}
}
