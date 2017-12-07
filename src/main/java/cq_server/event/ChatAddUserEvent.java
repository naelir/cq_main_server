package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "CHATADDUSER")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class ChatAddUserEvent extends BaseEvent {
	private String user;

	private Integer chatId;

	public ChatAddUserEvent() {
		super(EventType.CHATADDUSER);
	}

	public ChatAddUserEvent(final String user, final Integer chatId) {
		super(EventType.CHATADDUSER);
		this.user = user;
		this.chatId = chatId;
	}

	@XmlAttribute(name = "CHATID")
	public Integer getChatId() {
		return this.chatId;
	}

	@XmlAttribute(name = "USER")
	public String getUser() {
		return this.user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public void setChatId(final Integer chatId) {
		this.chatId = chatId;
	}
}
