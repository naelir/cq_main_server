package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "TIP")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class TipEvent extends BaseEvent {
	private Integer tip;

	private Integer human;

	public TipEvent() {
		super(EventType.TIP);
	}

	public TipEvent(final Integer tip, final Integer human) {
		super(EventType.TIP);
		this.tip = tip;
		this.human = human;
	}

	@XmlAttribute(name = "HUMAN")
	public Integer getHuman() {
		return this.human;
	}

	@XmlAttribute(name = "TIP")
	public Integer getTip() {
		return this.tip;
	}

	public void setTip(final Integer tip) {
		this.tip = tip;
	}

	public void setHuman(final Integer human) {
		this.human = human;
	}
}
