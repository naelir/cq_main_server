package cq_server.factory;

import cq_server.model.RawQuestion;
import cq_server.model.RawTip;

public interface IQuestionFactory {
	RawQuestion getFourOptionsQuestion();

	RawTip getTipQuestion();
}
