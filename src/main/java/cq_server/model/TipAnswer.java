package cq_server.model;

public final class TipAnswer {
	private final Integer answer;

	private final Integer id;

	private final Double time;

	public TipAnswer(final int id, final int answer, final long time) {
		super();
		this.id = id;
		this.answer = answer;
		this.time = time / 1000.0;
	}

	public Integer getAnswer() {
		return this.answer;
	}

	public Integer getId() {
		return this.id;
	}

	public Double getTime() {
		return this.time;
	}
}