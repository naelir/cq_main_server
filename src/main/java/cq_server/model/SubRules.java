package cq_server.model;

public enum SubRules {
	NORMAL(0), NOBASEATTACK(1), LASTMANSTANDING(2);
	private int value;

	private SubRules(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}