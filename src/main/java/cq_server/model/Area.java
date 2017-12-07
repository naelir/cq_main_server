package cq_server.model;

public final class Area {
	private int code;

	private int id;

	private AreaType type;

	private int value;

	public Area(final int id, final AreaType type, final int code, final int value) {
		this.id = id;
		this.type = type;
		this.code = code;
		this.value = value;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Area other = (Area) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

	public int getCode() {
		return this.code;
	}

	public int getId() {
		return this.id;
	}

	public int getState() {
		return this.code / 10;
	}

	public AreaType getType() {
		return this.type;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		return result;
	}

	public void setCode(final int code) {
		this.code = code;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setType(final AreaType type) {
		this.type = type;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Area [id=" + this.id + ", type=" + this.type + ", code=" + this.code + ", value=" + this.value + "]";
	}
}
