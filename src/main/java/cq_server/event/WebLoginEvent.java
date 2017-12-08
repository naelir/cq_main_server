package cq_server.event;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "WEBLOGIN")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class WebLoginEvent extends BaseEvent {
	private Integer userid;

	private String sessionid;

	private Integer waithallid;

	private Integer reconnect;

	private String userName;

	public WebLoginEvent() {
		super(EventType.WEBLOGIN);
	}

	public WebLoginEvent(final Integer userid, final String sessionid, final Integer waithallid,
			final Integer reconnect, final String userName) {
		super(EventType.WEBLOGIN);
		this.userid = userid;
		this.sessionid = sessionid;
		this.waithallid = waithallid;
		this.reconnect = reconnect;
		this.userName = userName;
	}

	@XmlAttribute(name = "RECONNECT")
	@NotNull
	public Integer getReconnect() {
		return this.reconnect;
	}

	@XmlAttribute(name = "SID")
	@NotNull
	public String getSessionid() {
		return this.sessionid;
	}

	@XmlAttribute(name = "USERID")
	@NotNull
	public Integer getUserid() {
		return this.userid;
	}

	@XmlAttribute(name = "USERNAME")
	@NotNull
	public String getUserName() {
		return this.userName;
	}

	@XmlAttribute(name = "WHID")
	@NotNull
	public Integer getWaithallid() {
		return this.waithallid;
	}

	public void setReconnect(final Integer reconnect) {
		this.reconnect = reconnect;
	}

	public void setSessionid(final String sessionid) {
		this.sessionid = sessionid;
	}

	public void setUserid(final Integer userid) {
		this.userid = userid;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public void setWaithallid(final Integer waithallid) {
		this.waithallid = waithallid;
	}
}
