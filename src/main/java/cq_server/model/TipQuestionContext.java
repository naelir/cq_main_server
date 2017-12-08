package cq_server.model;

public class TipQuestionContext {
	private final RawTip rawTip;

	private final TipQuestion tipQuestion;

	private final TipInfo tipinfo;

	private final TipResult tipResult;

	public TipQuestionContext(
			final RawTip rawTip,
			final TipQuestion tipQuestion,
			final TipInfo tipinfo,
			final TipResult tipResult) {
		this.rawTip = rawTip;
		this.tipQuestion = tipQuestion;
		this.tipinfo = tipinfo;
		this.tipResult = tipResult;
	}

	public RawTip getRawTip() {
		return this.rawTip;
	}

	public TipInfo getTipinfo() {
		return this.tipinfo;
	}

	public TipQuestion getTipQuestion() {
		return this.tipQuestion;
	}

	public TipResult getTipResult() {
		return this.tipResult;
	}
}
