package cq_server.model;

public final class TipAnswer {
	private final Integer answer;

	private final Integer id;

	private final Double time;

	public TipAnswer(int id, int answer, long time) {
		super();
		this.id = id;
		this.answer = answer;
		this.time = time / 1000.0;
	}

	public Integer getAnswer() {
		return answer;
	}

	public Integer getId() {
		return id;
	}

	public Double getTime() {
		return time;
	}
}