package cq_server.model;

public final class GameSettings {
	private final boolean personal;

	private final int seppmessageid;

	private final OOPP oopp;

	private final Rules rules;

	private final SubRules subRules;

	private final CountryMap countryMap;

	public GameSettings(final Builder builder) {
		super();
		this.personal = builder.personal;
		this.seppmessageid = builder.seppmessageid;
		this.oopp = builder.oopp;
		this.rules = builder.rules;
		this.subRules = builder.subRules;
		this.countryMap = builder.countryMap;
	}

	public CountryMap getCountryMap() {
		return this.countryMap;
	}

	public OOPP getOopp() {
		return this.oopp;
	}

	public Rules getRules() {
		return this.rules;
	}

	public int getSeppmessageid() {
		return this.seppmessageid;
	}

	public SubRules getSubRules() {
		return this.subRules;
	}

	public boolean isPersonal() {
		return this.personal;
	}

	public static final class Builder {
		private boolean personal;

		private int seppmessageid;

		private OOPP oopp;

		private Rules rules;

		private SubRules subRules;

		private CountryMap countryMap;

		public Builder() {
			super();
		}

		public GameSettings build() {
			return new GameSettings(this);
		}

		public Builder setCountryMap(final CountryMap countryMap) {
			this.countryMap = countryMap;
			return this;
		}

		public Builder setOopp(final OOPP oopp) {
			this.oopp = oopp;
			return this;
		}

		public Builder setPersonal(final boolean personal) {
			this.personal = personal;
			return this;
		}

		public Builder setRules(final Rules rules) {
			this.rules = rules;
			return this;
		}

		public Builder setSeppmessageid(final int seppmessageid) {
			this.seppmessageid = seppmessageid;
			return this;
		}

		public Builder setSubRules(final SubRules subRules) {
			this.subRules = subRules;
			return this;
		}
	}
}
