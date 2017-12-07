package cq_server.factory;

import cq_server.model.QuestionContext;
import cq_server.model.TipQuestionContext;

public interface IQuestionContextFactory {
	QuestionContext createQuestionContext(int players, int offender, int deffender);

	TipQuestionContext createTipQuestionContext(int players);
}