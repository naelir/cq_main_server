package cq_server.model;

public class QuestionContext {
	private RawQuestion rawQuestion;

	private AnswerResult answerResult;

	private Question question;

	public QuestionContext(RawQuestion rawQuestion, AnswerResult answerResult, Question question) {
		this.rawQuestion = rawQuestion;
		this.answerResult = answerResult;
		this.question = question;
	}

	public AnswerResult getAnswerResult() {
		return answerResult;
	}

	public RawQuestion getRawQuestion() {
		return rawQuestion;
	}

	public Question getQuestion() {
		return question;
	}
}
