package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "RATEQUESTION")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class RateQuestionEvent extends BaseEvent {
	private int rate;

	public RateQuestionEvent() {
		super(EventType.RATEQUESTION);
	}

	public RateQuestionEvent(final int rate) {
		super(EventType.RATEQUESTION);
		this.rate = rate;
	}

	@XmlAttribute(name = "RATE")
	public int getRate() {
		return this.rate;
	}

	public void setRate(final int rate) {
		this.rate = rate;
	}
}
