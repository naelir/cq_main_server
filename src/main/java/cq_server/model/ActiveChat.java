package cq_server.model;

import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ACTIVECHAT")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class ActiveChat {
	private final AtomicInteger id;

	public ActiveChat() {
		this(0);
	}

	public ActiveChat(final int id) {
		this.id = new AtomicInteger(id);
	}

	@XmlAttribute(name = "ID")
	public int getId() {
		return this.id.get();
	}

	public void setId(final int id) {
		this.id.set(id);
	}
}
