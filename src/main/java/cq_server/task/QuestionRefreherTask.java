package cq_server.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.game.IQuestionsLoader;
import cq_server.model.Questions;
import cq_server.model.RawQuestion;
import cq_server.model.RawTip;

public class QuestionRefreherTask implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(QuestionRefreherTask.class);

	private final List<RawTip> tips;

	private final List<RawQuestion> questions;

	private final IQuestionsLoader questionsLoader;

	public QuestionRefreherTask(
			final List<RawQuestion> questions,
			final List<RawTip> tips,
			final IQuestionsLoader questionsLoader) {
		this.questions = questions;
		this.tips = tips;
		this.questionsLoader = questionsLoader;
	}

	@Override
	public void run() {
		final Questions questionsAll = this.questionsLoader.load();
		LOG.info("questions before refresh: {}, tips before refresh: {}", this.questions.size(), this.tips.size());
		this.questions.clear();
		final List<RawQuestion> rawQuestions = questionsAll.getRawQuestions();
		this.questions.addAll(rawQuestions);
		this.tips.clear();
		final List<RawTip> rawTips = questionsAll.getTips();
		this.tips.addAll(rawTips);
		LOG.info("questions after refresh: {}, tips after refresh: {}", this.questions.size(), this.tips.size());
	}
}
