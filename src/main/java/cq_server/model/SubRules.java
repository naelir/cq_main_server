package cq_server.model;


public enum SubRules {
	NORMAL(0), NOBASEATTACK(1), LASTMANSTANDING(2);
	private int value;

	private SubRules(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}