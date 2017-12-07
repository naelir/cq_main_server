package cq_server.model;

import java.util.Arrays;
import java.util.List;

public class Questions {

	private final List<RawTip> tips;

	private final List<RawQuestion> rawq;

	public Questions(final RawQuestion[] rawq, final RawTip[] tips) {
		this.rawq = Arrays.asList(rawq);
		this.tips = Arrays.asList(tips);
	}

	public Questions(final List<RawQuestion> rawq, final List<RawTip> tips) {
		this.rawq = rawq;
		this.tips = tips;
	}

	public List<RawTip> getTips() {
		return this.tips;
	}

	public List<RawQuestion> getRawQuestions() {
		return this.rawq;
	}

}
