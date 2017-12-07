package cq_server.factory;

import java.util.List;
import java.util.Random;

import cq_server.model.RawQuestion;
import cq_server.model.RawTip;

public class QuestionFactory implements IQuestionFactory {
	private static final RawTip DEFAULT_TIP = new RawTip("no tips", 1);

	private static final RawQuestion DEFAULT_QUESTION = new RawQuestion("no questions",
			new String[] { "$", "%", "/", "#" }, 1);

	private final List<RawTip> tips;

	private final List<RawQuestion> questions;

	private final Random generator;

	public QuestionFactory(final List<RawQuestion> questions, final List<RawTip> tips) {
		this.questions = questions;
		this.tips = tips;
		this.generator = new Random();
	}

	@Override
	public RawQuestion getFourOptionsQuestion() {
		final int size = this.questions.size();
		if (size == 0)
			return QuestionFactory.DEFAULT_QUESTION;
		else
			return this.questions.get(this.generator.nextInt(size));
	}

	@Override
	public RawTip getTipQuestion() {
		final int size = this.tips.size();
		if (size == 0)
			return QuestionFactory.DEFAULT_TIP;
		else
			return this.tips.get(this.generator.nextInt(size));
	}
}
