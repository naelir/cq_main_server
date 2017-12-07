package cq_server.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.game.BasePlayer;
import cq_server.game.Chat;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.*;

public final class WaithallRefreshTask implements Runnable {
	private final Map<Integer, BasePlayer> loggedPlayers;

	private final Map<Integer, Chat> chats;

	private final UserList usersList;

	private final IOutputMessageHandler outputMessageHandler;

	private final Map<Integer, SepRoom> separateRooms;

	private final Map<Integer, GameRoom> gameRooms;

	private final AtomicBoolean isWhNeedRefresh;

	public WaithallRefreshTask(final Builder builder) {
		this.loggedPlayers = builder.loggedPlayers;
		this.chats = builder.chats;
		this.usersList = builder.usersList;
		this.outputMessageHandler = builder.outputMessageHandler;
		this.separateRooms = builder.separateRooms;
		this.gameRooms = builder.gameRooms;
		this.isWhNeedRefresh = builder.isWhNeedRefresh;
	}

	public void run() {
		if (this.isWhNeedRefresh.getAndSet(false))
			for (final BasePlayer player : this.loggedPlayers.values())
				if (player.isListen() && player.getPage().equals(Page.WAITHALL)) {
					player.setListen(false);
					final List<Object> state = new ArrayList<>();
					state.add(player.getListenChannel());
					state.add(player.getRights());
					for (final GameRoom gameRoom : this.gameRooms.values())
						state.add(gameRoom);
					for (final SepRoom sepRoom : this.separateRooms.values())
						state.add(sepRoom);
					state.add(this.usersList);
					final int mstate = player.getMstate();
					int newMstate = 0;
					for (final Chat chat : this.chats.values())
						if (chat.contains(player)) {
							state.add(chat);
							final ActiveChat activeChat = player.getActiveChat();
							if (chat.getId() == activeChat.getId()) {
								state.add(activeChat);
								final List<ChatMsg> chatMsgs = chat.missedMessages(mstate);
								for (final ChatMsg chatMsg : chatMsgs)
									state.add(chatMsg);
								newMstate = chat.getMstate();
							}
						}
					final WaitState waitstate = player.getWaitState();
					state.add(waitstate);
					final NoChat noChat = player.getNoChat();
					if (noChat != null)
						state.add(noChat);
					player.setMstate(newMstate);
					player.setUlState(this.usersList.getUlState());
					this.outputMessageHandler.sendMessage(player, state);
				}
	}

	public static final class Builder {
		private Map<Integer, BasePlayer> loggedPlayers;

		private Map<Integer, Chat> chats;

		private UserList usersList;

		private IOutputMessageHandler outputMessageHandler;

		private Map<Integer, SepRoom> separateRooms;

		private Map<Integer, GameRoom> gameRooms;

		private AtomicBoolean isWhNeedRefresh;

		public WaithallRefreshTask build() {
			return new WaithallRefreshTask(this);
		}

		public Builder setChats(final Map<Integer, Chat> chats) {
			this.chats = chats;
			return this;
		}

		public Builder setGameRooms(final Map<Integer, GameRoom> gameRooms) {
			this.gameRooms = gameRooms;
			return this;
		}

		public Builder setIsWhNeedRefresh(final AtomicBoolean isWhNeedRefresh) {
			this.isWhNeedRefresh = isWhNeedRefresh;
			return this;
		}

		public Builder setLoggedPlayers(final Map<Integer, BasePlayer> loggedPlayers) {
			this.loggedPlayers = loggedPlayers;
			return this;
		}

		public Builder setOutputMessageHandler(final IOutputMessageHandler outputMessageHandler) {
			this.outputMessageHandler = outputMessageHandler;
			return this;
		}

		public Builder setSeparateRooms(final Map<Integer, SepRoom> separateRooms) {
			this.separateRooms = separateRooms;
			return this;
		}

		public Builder setUsersList(final UserList usersList) {
			this.usersList = usersList;
			return this;
		}
	}
}
