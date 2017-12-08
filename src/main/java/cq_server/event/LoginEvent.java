package cq_server.event;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "LOGIN")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class LoginEvent extends BaseEvent {
	private String user;

	private String pass;

	private Integer waithallid;

	private Integer reconnect;

	public LoginEvent() {
		super(EventType.LOGIN);
	}

	public LoginEvent(final String user, final String pass, final Integer waithallid, final Integer reconnect) {
		super(EventType.LOGIN);
		this.user = user;
		this.pass = pass;
		this.waithallid = waithallid;
		this.reconnect = reconnect;
	}

	@XmlAttribute(name = "PASS")
	@NotNull
	public String getPass() {
		return this.pass;
	}

	@XmlAttribute(name = "RECONNECT")
	@NotNull
	public Integer getReconnect() {
		return this.reconnect;
	}

	@XmlAttribute(name = "USER")
	@NotNull
	public String getUser() {
		return this.user;
	}

	@XmlAttribute(name = "WHID")
	@NotNull
	public Integer getWaithallid() {
		return this.waithallid;
	}

	public void setPass(final String pass) {
		this.pass = pass;
	}

	public void setReconnect(final Integer reconnect) {
		this.reconnect = reconnect;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public void setWaithallid(final Integer waithallid) {
		this.waithallid = waithallid;
	}
}
