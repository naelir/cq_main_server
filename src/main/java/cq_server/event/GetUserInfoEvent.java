package cq_server.event;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "GETUSERINFO")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class GetUserInfoEvent extends BaseEvent {
	private String user;

	public GetUserInfoEvent() {
		super(EventType.GETUSERINFO);
	}

	public GetUserInfoEvent(final String user) {
		super(EventType.GETUSERINFO);
		this.user = user;
	}

	@XmlAttribute(name = "USER")
	@NotNull
	public String getUser() {
		return this.user;
	}

	public void setUser(final String user) {
		this.user = user;
	}
}
