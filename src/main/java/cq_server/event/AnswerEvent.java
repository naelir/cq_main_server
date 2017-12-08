package cq_server.event;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

/*
 * <CMD VC="3FAD7D701BAAB2EA41F2A070" CONN="4692455" MSGNUM="4" BANN="0" />
 * <ANSWER ANSWER="2" />
 */
@XmlRootElement(name = "ANSWER")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class AnswerEvent extends BaseEvent {
	private Integer answer = 0;

	public AnswerEvent() {
		super(EventType.ANSWER);
	}

	public AnswerEvent(final Integer answer) {
		super(EventType.ANSWER);
		this.answer = answer;
	}

	@XmlAttribute(name = "ANSWER")
	@NotNull
	public Integer getAnswer() {
		return this.answer;
	}

	public void setAnswer(final Integer answer) {
		this.answer = answer;
	}
}
