package cq_server.game;

import static cq_server.Assertions.notNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.Assertions;
import cq_server.event.CmdChannel;
import cq_server.event.ListenChannel;
import cq_server.model.*;

public abstract class BasePlayer {
	private static final Logger LOG = LoggerFactory.getLogger(BasePlayer.class);

	private final ActiveChat activeChat;

	private BaseChannel cmdChannel;

	private final int id;

	private final String ip;

	private final AtomicBoolean isListen;

	private BaseChannel listenChannel;

	private final AtomicBoolean loggedIn;

	private final AtomicInteger mstate;

	private final MyData mydata;

	private NoChat noChat;

	private Page page;

	private final AtomicBoolean ready;

	private final Rights rights;

	private final AtomicInteger ulstate;

	private final WaitState waitState;

	public BasePlayer(final Integer id, final String ip, final String name) {
		this.id = notNull("id", id);
		this.mydata = new MyData(id, name);
		this.ip = ip;
		this.waitState = new WaitState(0, 0);
		this.page = Page.WAITHALL;
		this.rights = new Rights();
		this.ulstate = new AtomicInteger(0);
		this.ready = new AtomicBoolean(true);
		this.activeChat = new ActiveChat(0);
		this.loggedIn = new AtomicBoolean(false);
		this.isListen = new AtomicBoolean(false);
		this.cmdChannel = new CmdChannel(0, 0, 0);
		this.listenChannel = new ListenChannel(0, 0, 0);
		this.mstate = new AtomicInteger(0); 
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final BasePlayer other = (BasePlayer) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

	public ActiveChat getActiveChat() {
		return this.activeChat;
	}

	public synchronized BaseChannel getCmdChannel() {
		return this.cmdChannel;
	}

	public int getId() {
		return this.id;
	}

	public String getIp() {
		return this.ip;
	}

	public synchronized BaseChannel getListenChannel() {
		return this.listenChannel;
	}

	public int getMstate() {
		return this.mstate.get();
	}

	public MyData getMydata() {
		return this.mydata;
	}

	public String getName() {
		return this.mydata.getName();
	}

	public synchronized NoChat getNoChat() {
		return this.noChat;
	}

	public synchronized Page getPage() {
		return this.page;
	}

	public Rights getRights() {
		return this.rights;
	}

	public int getSepRoomId() {
		return this.waitState.getSepRoomSel();
	}

	public int getUlStateId() {
		return this.ulstate.get();
	}

	public WaitState getWaitState() {
		return this.waitState;
	}

	public abstract void handle(List<Object> data);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		return result;
	}

	public boolean isListen() {
		return this.isListen.get();
	}

	public boolean isLoggedIn() {
		return this.loggedIn.get();
	}

	public boolean isReady() {
		return this.ready.get();
	}

	public void setActiveChat(final int chatId) {
		this.activeChat.setId(chatId);
	}

	public synchronized void setChannel(final BaseChannel channel) {
		Assertions.notNull("channel", channel);
		if (channel instanceof ListenChannel) {
			this.listenChannel = channel;
			this.isListen.set(true);
			;
		} else
			this.cmdChannel = channel;
		if (channel.getConnid().equals(0))
			channel.setConnId(this.id);
	}

	public void setFriendList(final String user, final ModFriendOperation operation) {
		switch (operation) {
		case FRIEND:
			break;
		case IGNORE:
			break;
		default:
			break;
		}
	}

	public void setListen(final boolean isListen) {
		this.isListen.set(isListen);
	}

	public void setLoggedIn(final boolean loggedIn) {
		this.loggedIn.set(loggedIn);
	}

	public void setMstate(final int mstate) {
		this.mstate.set(mstate);
	}

	public void setName(final String name) {
		this.mydata.setName(name);
	}

	public synchronized void setNoChat(final NoChat noChat) {
		this.noChat = noChat;
	}

	public synchronized void setPage(final Page page) {
		this.page = page;
		this.waitState.setSepRoomSel(0);
		this.activeChat.setId(0);
		this.mstate.set(0);
	}

	public void setReady(final boolean ready) {
		LOG.debug("{} is ready: {}", this.mydata.getName(), ready);
		this.ready.set(ready);
	}

	public void setUlState(final int ulState) {
		this.ulstate.set(ulState);
	}

	@Override
	public String toString() {
		return "Player [id=" + this.id + ", name=" + this.mydata.getName() + ", ip=" + this.ip + "]";
	}
}
