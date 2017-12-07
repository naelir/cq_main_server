package cq_server.model;

public class TipQuestionContext {
	private final RawTip rawTip;

	private final TipQuestion tipQuestion;

	private final TipInfo tipinfo;

	private final TipResult tipResult;

	public TipQuestionContext(RawTip rawTip, TipQuestion tipQuestion, TipInfo tipinfo, TipResult tipResult) {
		this.rawTip = rawTip;
		this.tipQuestion = tipQuestion;
		this.tipinfo = tipinfo;
		this.tipResult = tipResult;
	}

	public RawTip getRawTip() {
		return rawTip;
	}

	public TipQuestion getTipQuestion() {
		return tipQuestion;
	}

	public TipInfo getTipinfo() {
		return tipinfo;
	}

	public TipResult getTipResult() {
		return tipResult;
	}
}
