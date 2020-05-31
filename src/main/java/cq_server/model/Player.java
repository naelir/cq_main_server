package cq_server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player {
	private static final Logger LOG = LoggerFactory.getLogger(Player.class);

	private final ActiveChat activeChat;

	private final int id;

	private final String ip;

	private boolean isListen;

	private boolean loggedIn;

	private Integer mstate;

	private final MyData mydata;

	private NoChat noChat;

	private Page page;

	private boolean ready;

	private final Rights rights;

	private Integer ulstate;

	private final WaitState waitState;

	private final Type type;

	public Player(final Integer id, final Type type, final String ip, final String name) {
		this.type = type;
		this.id = id;
		this.mydata = new MyData(id, name);
		this.ip = ip;
		this.waitState = new WaitState(0, 0);
		this.page = Page.WAITHALL;
		this.rights = new Rights();
		this.ulstate = 0;
		this.ready = true;
		this.activeChat = new ActiveChat(0);
		this.loggedIn = false;
		this.isListen = true;
		this.mstate = 0;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Player other = (Player) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

	public ActiveChat getActiveChat() {
		return this.activeChat;
	}

	public int getId() {
		return this.id;
	}

	public String getIp() {
		return this.ip;
	}

	public int getMstate() {
		return this.mstate;
	}

	public MyData getMydata() {
		return this.mydata;
	}

	public String getName() {
		return this.mydata.getName();
	}

	public NoChat getNoChat() {
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

	public Type getType() {
		return this.type;
	}

	public int getUlStateId() {
		return this.ulstate;
	}

	public WaitState getWaitState() {
		return this.waitState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		return result;
	}

	public boolean isListen() {
		return this.isListen;
	}

	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	public boolean isReady() {
		return this.ready;
	}

	public void setActiveChat(final int chatId) {
		this.activeChat.setId(chatId);
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
		this.isListen = isListen;
		LOG.trace("{} is listen: {}", this.mydata.getName(), isListen);
	}

	public void setLoggedIn(final boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void setMstate(final int mstate) {
		this.mstate = mstate;
	}

	public void setName(final String name) {
		this.mydata.setName(name);
	}

	public void setNoChat(final NoChat noChat) {
		this.noChat = noChat;
	}

	public synchronized void setPage(final Page page) {
		this.page = page;
		this.waitState.setSepRoomSel(0);
		this.activeChat.setId(0);
		this.mstate = 0;
	}

	public void setReady(final boolean ready) {
		this.ready = ready;
		LOG.trace("{} is ready: {}", this.mydata.getName(), ready);
	}

	public void setUlState(final int ulState) {
		this.ulstate = ulState;
	}

	@Override
	public String toString() {
		return "Player [id=" + this.id + ", name=" + this.mydata.getName() + ", ip=" + this.ip + "]";
	}

	public enum Type {
		ONLINE, ROBOT;
	}
}
