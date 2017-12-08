package cq_server.model;

public enum Rules {
	LONG(1), SHORT(2), DUEL(3);
	private int value;

	private Rules(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
