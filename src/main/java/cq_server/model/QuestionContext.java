package cq_server.model;

public class QuestionContext {
	private final Question question;

	public QuestionContext(final Question question) {
		this.question = question;
	}

	public Question getQuestion() {
		return this.question;
	}
}
