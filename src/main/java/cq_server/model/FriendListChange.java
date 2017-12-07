package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FRIENDLISTCHANGE")
public final class FriendListChange {
	@XmlAttribute(name = "FRIENDS")
	private final String friends;

	@XmlAttribute(name = "IGNORED")
	private final String ignored;

	public FriendListChange() {
		this(null);
	}

	public FriendListChange(String friends) {
		this.friends = friends;
		this.ignored = "";
	}
}
