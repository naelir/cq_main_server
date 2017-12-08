package cq_server.event;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "SETACTIVECHAT")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class SetActiveChatEvent extends BaseEvent {
	private Integer chatId;

	private Integer mstate;

	public SetActiveChatEvent() {
		super(EventType.SETACTIVECHAT);
	}

	public SetActiveChatEvent(final Integer chatId, final Integer mstate) {
		super(EventType.SETACTIVECHAT);
		this.chatId = chatId;
		this.mstate = mstate;
	}

	@XmlAttribute(name = "ID")
	@NotNull
	public Integer getChatId() {
		return this.chatId;
	}

	@XmlAttribute(name = "MSTATE")
	@NotNull
	public Integer getMstate() {
		return this.mstate;
	}

	public void setChatId(final Integer chatId) {
		this.chatId = chatId;
	}

	public void setMstate(final Integer mstate) {
		this.mstate = mstate;
	}
}
