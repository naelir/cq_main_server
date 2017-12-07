package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "CHATCLOSE")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class ChatCloseEvent extends BaseEvent {
	private Integer chatId;

	private Integer activeChat;

	private Integer mstate;

	public ChatCloseEvent() {
		super(EventType.CHATCLOSE);
	}

	public ChatCloseEvent(final Integer chatId, final Integer activeChat, final Integer mstate) {
		super(EventType.CHATCLOSE);
		this.chatId = chatId;
		this.activeChat = activeChat;
		this.mstate = mstate;
	}

	@XmlAttribute(name = "ACTIVECHAT")
	public Integer getActiveChat() {
		return this.activeChat;
	}

	@XmlAttribute(name = "ID")
	public Integer getChatId() {
		return this.chatId;
	}

	@XmlAttribute(name = "MSTATE")
	public Integer getMstate() {
		return this.mstate;
	}

	public void setChatId(final Integer chatId) {
		this.chatId = chatId;
	}

	public void setActiveChat(final Integer activeChat) {
		this.activeChat = activeChat;
	}

	public void setMstate(final Integer mstate) {
		this.mstate = mstate;
	}
}
