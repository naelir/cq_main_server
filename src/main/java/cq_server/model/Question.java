package cq_server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "QUESTION")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class Question {
	private static final RawQuestion DEFAULT_QUESTION = new RawQuestion("", new String[] { "0", "1", "2", "3" }, 0);

	private final int allowMark;

	private final String op1;

	private final String op2;

	private final String op3;

	private final String op4;

	private final String q;

	private final AnswerResult answerresult;

	public Question() {
		this(DEFAULT_QUESTION, new AnswerResult());
	}

	public Question(RawQuestion question, AnswerResult answerresult) {
		super();
		this.answerresult = answerresult;
		this.allowMark = 0;
		this.q = question.getQuestion();
		this.op1 = question.getOptions()[0];
		this.op2 = question.getOptions()[1];
		this.op3 = question.getOptions()[2];
		this.op4 = question.getOptions()[3];
	}

	@XmlAttribute(name = "ALLOWMARK")
	public int getAllowMark() {
		return allowMark;
	}

	@XmlAttribute(name = "OP1")
	public String getOp1() {
		return op1;
	}

	@XmlAttribute(name = "OP2")
	public String getOp2() {
		return op2;
	}

	@XmlAttribute(name = "OP3")
	public String getOp3() {
		return op3;
	}

	@XmlAttribute(name = "OP4")
	public String getOp4() {
		return op4;
	}

	@XmlAttribute(name = "QUESTION")
	public String getQ() {
		return q;
	}

	public AnswerResult getAnswerresult() {
		return answerresult;
	}
}
