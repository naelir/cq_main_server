package cq_server.factory;

public class NameFormatter {
	private static final int USER_NAME_MAX_LENGTH = 12;

	private final int userNameMaxLength;

	public NameFormatter() {
		this.userNameMaxLength = USER_NAME_MAX_LENGTH;
	}

	public String format(final String name, final String id) {
		if (name == null || name.isEmpty())
			return id;
		else if (name.length() > this.userNameMaxLength)
			return name.substring(0, this.userNameMaxLength) + "-" + id;
		else
			return name + "-" + id;
	}
}
