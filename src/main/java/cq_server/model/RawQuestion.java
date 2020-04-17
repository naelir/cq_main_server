package cq_server.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class RawQuestion {

	@JsonProperty("question")
	private String question;

	@JsonProperty("options")
	private String[] options;

	@JsonProperty("trueAnswer")
	private int trueAnswer;

	public RawQuestion() {
	}

	public RawQuestion(final String question, final String[] options, final int trueAnswer) {
		super();
		this.question = question;
		this.options = options;
		this.trueAnswer = trueAnswer;
	}

	public int getTrueAnswer() {
		return this.trueAnswer;
	}

	public String getQuestion() {
		return this.question;
	}

	public String[] getOptions() {
		return this.options;
	}

	@Override
	public String toString() {
		return "RawQuestion [question=" + this.question + ", options=" + Arrays.toString(this.options) + ", trueAnswer="
				+ this.trueAnswer + "]";
	}
}
