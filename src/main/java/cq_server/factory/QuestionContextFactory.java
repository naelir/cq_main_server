package cq_server.factory;

import cq_server.model.*;

public class QuestionContextFactory implements IQuestionContextFactory {
	private final IQuestionFactory questionFactory;

	public QuestionContextFactory(final IQuestionFactory factory) {
		this.questionFactory = factory;
	}

	@Override
	public QuestionContext createQuestionContext(final int players, final int offender, final int deffender) {
		final RawQuestion rawQuestion = this.questionFactory.getFourOptionsQuestion();
		final int trueAnswer = rawQuestion.getTrueAnswer();
		final AnswerResult answerResult = new AnswerResult(trueAnswer, offender, deffender, players);
		final Question question = new Question(rawQuestion, answerResult);
		return new QuestionContext(rawQuestion, answerResult, question);
	}

	@Override
	public TipQuestionContext createTipQuestionContext(final int players) {
		final RawTip rawTip = this.questionFactory.getTipQuestion();
		final String question = rawTip.getQuestion();
		final TipQuestion tipQuestion = new TipQuestion(question, players, 0, 1);
		final int answer = rawTip.getAnswer();
		final TipInfo tipinfo = new TipInfo(answer, players);
		final TipResult tipResult = new TipResult(answer);
		return new TipQuestionContext(rawTip, tipQuestion, tipinfo, tipResult);
	}
}
