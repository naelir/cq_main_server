package cq_server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TIPQUESTION")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TipQuestion {
	private String tip;

	private int nonmarkable;

	private int allowmark;

	public TipQuestion() {
		// TODO Auto-generated constructor stub
	}

	public TipQuestion(final String tip, final int players, final int nonmarkable, final int allowmark) {
		super();
		this.nonmarkable = nonmarkable;
		this.allowmark = allowmark;
		this.tip = tip;
	}

	@XmlAttribute(name = "ALLOWMARK")
	public int getAllowmark() {
		return this.allowmark;
	}

	@XmlAttribute(name = "NONMARKABLE")
	public int getNonmarkable() {
		return this.nonmarkable;
	}

	@XmlAttribute(name = "QUESTION")
	public String getQuestion() {
		return this.tip;
	}
}
