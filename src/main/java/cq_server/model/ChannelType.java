package cq_server.model;

public enum ChannelType {
	LISTEN("LISTEN"), CMD("CMD");

	private String value;

	private ChannelType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}