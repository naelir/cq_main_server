package cq_server;

public class Assertions {
	public static <T> T notNull(final String description, final T o) {
		if (o == null)
			throw new NullPointerException(description + "cannot be null");
		return o;
	}
}
