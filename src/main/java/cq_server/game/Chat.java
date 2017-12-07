package cq_server.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.Assertions;
import cq_server.model.ChatMsg;

@XmlRootElement(name = "CHATROOM")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Chat {
	private static final int MAX_MISSED_MESSAGES = 50;

	private static final int DEFAULT_USTATE = 1;

	private static final int DEFAULT_MSTATE = 0;

	private BasePlayer host;

	private int id;

	private List<ChatMsg> messages;

	private AtomicInteger mstate;

	private List<BasePlayer> users;

	private AtomicInteger ustate;

	private int maxMissedMessages;

	public Chat() {
	}

	public Chat(final int id, final BasePlayer host) {
		this.id = id;
		this.host = host;
		this.maxMissedMessages = MAX_MISSED_MESSAGES;
		this.messages = new CopyOnWriteArrayList<>();
		this.mstate = new AtomicInteger(DEFAULT_MSTATE);
		this.ustate = new AtomicInteger(DEFAULT_USTATE);
		this.users = new CopyOnWriteArrayList<>();
	}

	public boolean addMessage(final ChatMsg message) {
		Assertions.notNull("message", message);
		final int mid = this.mstate.incrementAndGet();
		message.setId(mid);
		return this.messages.add(message);
	}

	public boolean addUser(final BasePlayer chatter) {
		Assertions.notNull("chatter", chatter);
		if (!this.users.contains(chatter) && this.users.add(chatter)) {
			this.ustate.incrementAndGet();
			return true;
		}
		return false;
	}

	public boolean contains(final BasePlayer chatter) {
		Assertions.notNull("chatter", chatter);
		return this.users.contains(chatter);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Chat other = (Chat) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

	@XmlAttribute(name = "FUSER")
	public String getHost() {
		return this.host != null ? this.host.getName() : null;
	}

	@XmlAttribute(name = "ID")
	public int getId() {
		return this.id;
	}

	@XmlAttribute(name = "MSTATE")
	public int getMstate() {
		return this.mstate.get();
	}

	@XmlAttribute(name = "USERS")
	public int getSize() {
		return this.users.size();
	}

	@XmlAttribute(name = "USTATE")
	public int getUstate() {
		return this.ustate.get();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		return result;
	}

	public boolean isEmpty() {
		return this.users.isEmpty();
	}

	public List<ChatMsg> missedMessages(final int mstateId) {
		//@formatter:off
		final int currentState = this.mstate.get(); 
		if (mstateId == 0 && currentState > this.maxMissedMessages)
			return this.messages
					.stream()
					.filter(message -> message.getId() > currentState - this.maxMissedMessages)
					.collect(Collectors.toList());
		else
			return this.messages
					.stream()
					.filter(message -> message.getId() > mstateId)
					.collect(Collectors.toList());
		//@formatter:off
	}

	public boolean remove(final BasePlayer player) {
		if (this.users.remove(player)) { 
 			this.ustate.incrementAndGet();
 			return true;
		}
		return false; 
	}
 

	@Override
	public String toString() {
		return "Chat [messages=" + this.messages + ", mstate=" + this.mstate + ", ustate=" + this.ustate + ", players="
				+ this.users + ", id=" + this.id + ", creator=" + this.host + "]";
	}
}
