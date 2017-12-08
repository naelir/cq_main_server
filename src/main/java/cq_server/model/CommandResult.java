package cq_server.model;

public enum CommandResult {
	OK(0), ERROR(1);
	private final int value;

	private CommandResult(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
