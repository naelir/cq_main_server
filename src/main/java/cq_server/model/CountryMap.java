package cq_server.model;

public enum CountryMap {
	BG("BG");
	private final String name;

	CountryMap(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
