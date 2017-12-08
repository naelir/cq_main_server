package cq_server.test;

import cq_server.factory.IQuestionsLoader;
import cq_server.factory.OnlineQuestionsLoader;
import cq_server.model.Questions;

public class HttpTest {
	public static void main(final String[] args) {
		final String questionsApiEndpoint = "http://localhost:8080/api/questions";
		final String tipsApiEndpoint = "http://localhost:8080/api/tips";
		final IQuestionsLoader questionsLoader = new OnlineQuestionsLoader(questionsApiEndpoint, tipsApiEndpoint);
		//@formatter:on
		final Questions questions = questionsLoader.load();
		System.out.println(questions.getRawQuestions().size());
		System.out.println(questions.getTips().size());
	}
}
