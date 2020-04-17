package cq_server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class RawTip {

	@JsonProperty("question")
	private String question;

	@JsonProperty("answer")
	private int answer;

	public RawTip() {
	}

	public RawTip(final String question, final int answer) {
		super();
		this.question = question;
		this.answer = answer;
	}

	public String getQuestion() {
		return this.question;
	}

	public int getAnswer() {
		return this.answer;
	}

	@Override
	public String toString() {
		return "RawTip [question=" + this.question + ", answer=" + this.answer + "]";
	}
}
