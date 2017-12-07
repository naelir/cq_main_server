package cq_server.model;

public enum CountryMap {
	BG("BG");
	private final String name;

	CountryMap(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
