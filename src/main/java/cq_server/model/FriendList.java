package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FRIENDLIST")
public final class FriendList {
	@XmlAttribute(name = "USERS")
	private final String users;

	public FriendList() {
		super();
		this.users = "";
	}
}