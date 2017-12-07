package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "MODFRIENDLIST")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class ModFriendListEvent extends BaseEvent {
	private String user;

	private String ignored;

	public ModFriendListEvent() {
		super(EventType.MODFRIENDLIST);
	}

	public ModFriendListEvent(final String user, final String ignored) {
		super(EventType.MODFRIENDLIST);
		this.user = user;
		this.ignored = ignored;
	}

	@XmlAttribute(name = "IGNORED")
	public String getIgnored() {
		return this.ignored;
	}

	@XmlAttribute(name = "FRIENDS")
	public String getUser() {
		return this.user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public void setIgnored(final String ignored) {
		this.ignored = ignored;
	}
}
