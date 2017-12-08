package cq_server.model;

public enum OOPP {
	ANYONE(1), HASROBOT(2), WITH_INVITED_PLAYERS(3);
	private int value;

	private OOPP(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}