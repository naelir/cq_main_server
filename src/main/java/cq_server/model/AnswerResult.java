package cq_server.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ANSWERRESULT")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AnswerResult {
	private Map<Integer, Integer> answers;

	private Integer offender;

	private Integer deffender;

	private Integer p1;

	private Integer p2;

	private Integer p3;

	private Integer good;

	private AttackStatus status;

	private Integer players;

	public AnswerResult() {
	}

	public AnswerResult(final Integer good, final Integer offender, final Integer deffender, final Integer players) {
		super();
		this.good = good;
		this.offender = offender;
		this.deffender = deffender;
		this.players = players;
		this.answers = new ConcurrentHashMap<>(players.intValue());
		this.status = AttackStatus.NA;
	}

	@XmlAttribute(name = "GOOD")
	public Integer getGood() {
		return this.good;
	}

	@XmlAttribute(name = "PLAYER1")
	public Integer getP1() {
		return this.p1;
	}

	@XmlAttribute(name = "PLAYER2")
	public Integer getP2() {
		return this.p2;
	}

	@XmlAttribute(name = "PLAYER3")
	public Integer getP3() {
		return this.p3;
	}

	public AttackStatus getStatus() {
		return this.status;
	}

	public boolean isAnswered(final Integer playerId) {
		return this.answers.get(playerId) != null;
	}

	public void setAnswer(final Integer playerId, final Integer answer) {
		this.answers.put(playerId, answer);
		switch (playerId.intValue()) {
		case 1:
			this.p1 = answer;
			break;
		case 2:
			this.p2 = answer;
			break;
		case 3:
			this.p3 = answer;
			break;
		default:
			break;
		}
		if (this.answers.size() == this.players.intValue()) {
			final Integer offenderAnswer = this.answers.get(this.offender);
			final Integer deffenderAnswer = this.answers.get(this.deffender);
			if (!offenderAnswer.equals(this.good) && !deffenderAnswer.equals(this.good))
				this.status = AttackStatus.BOTH_FAIL;
			else if (!offenderAnswer.equals(this.good) && deffenderAnswer.equals(this.good))
				this.status = AttackStatus.OFFENDER_FAIL;
			else if (offenderAnswer.equals(this.good) && deffenderAnswer.equals(this.good))
				this.status = AttackStatus.TIE;
			else
				this.status = AttackStatus.SUCCESS;
		}
	}

	@Override
	public String toString() {
		return "AnswerResult [answers=" + this.answers + ", offender=" + this.offender + ", deffender=" + this.deffender
				+ ", p1=" + this.p1 + ", p2=" + this.p2 + ", p3=" + this.p3 + ", good=" + this.good + "]";
	}
}