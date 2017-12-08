package cq_server.game;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.ChatMsg;
import cq_server.model.Player;

@XmlRootElement(name = "CHATROOM")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Chat {
	private static final int MAX_MISSED_MESSAGES = 50;

	private static final int DEFAULT_USTATE = 1;

	private static final int DEFAULT_MSTATE = 0;

	private Player host;

	private int id;

	private Queue<ChatMsg> messages;

	private AtomicInteger mstate;

	private Set<Player> users;

	private AtomicInteger ustate;

	private int maxMissedMessages;

	public Chat() {
	}

	public Chat(final int id, final Player host) {
		this.id = id;
		this.host = host;
		this.maxMissedMessages = MAX_MISSED_MESSAGES;
		this.messages = new ConcurrentLinkedQueue<>();
		this.mstate = new AtomicInteger(DEFAULT_MSTATE);
		this.ustate = new AtomicInteger(DEFAULT_USTATE);
		this.users = new CopyOnWriteArraySet<>();
	}

	public boolean addMessage(final ChatMsg message) {
		if (this.messages.offer(message)) {
			if (this.messages.size() > this.maxMissedMessages)
				this.messages.poll();
			final int mid = this.mstate.incrementAndGet();
			message.setId(mid);
			return true;
		}
		return false;
	}

	public boolean addUser(final Player user) {
		if (this.users.add(user)) {
			this.ustate.incrementAndGet();
			return true;
		}
		return false;
	}

	public boolean contains(final Player chatter) {
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

	public Set<Player> getUsers() {
		return this.users;
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
		final List<ChatMsg> msgs;
		if (mstateId == 0 && currentState > this.maxMissedMessages)
			msgs = this.messages
					.stream()
					.filter(message -> message.getId() > currentState - this.maxMissedMessages)
					.collect(Collectors.toList());
		else
			msgs = this.messages
					.stream()
					.filter(message -> message.getId() > mstateId)
					.collect(Collectors.toList());
		return msgs;
		//@formatter:off
	}

	public boolean remove(final Player player) {
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
