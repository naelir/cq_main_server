package cq_server.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cq_server.game.Chat;
import cq_server.handler.IOutEventHandler;
import cq_server.model.*;

public final class WaithallRefreshTask {
	private final Map<Integer, Player> loggedPlayers;

	private final Map<Integer, Chat> chats;

	private final UserList usersList;

	private final IOutEventHandler outEventHandler;

	private final Map<Integer, SepRoom> separateRooms;

	private final Map<Integer, GameRoom> gameRooms;

	public WaithallRefreshTask(final Builder builder) {
		this.loggedPlayers = builder.loggedPlayers;
		this.chats = builder.chats;
		this.usersList = builder.usersList;
		this.outEventHandler = builder.outEventHandler;
		this.separateRooms = builder.separateRooms;
		this.gameRooms = builder.gameRooms;
	}

	public void run() {
		final List<Player> collect = this.loggedPlayers.values()
			.stream()
			.filter(element -> Page.WAITHALL.equals(element.getPage()))
			.collect(Collectors.toList());
		this.run(collect);
	}

	public void run(final Collection<Player> players) {
		for (final Player player : players) {
			player.setListen(false);
			final List<Object> state = new ArrayList<>();
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
			this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.LISTEN, player, state));
		}
	}

	public static final class Builder {
		Map<Integer, Player> loggedPlayers = Collections.emptyMap();

		Map<Integer, Chat> chats = Collections.emptyMap();

		UserList usersList;

		IOutEventHandler outEventHandler;

		Map<Integer, SepRoom> separateRooms = Collections.emptyMap();

		Map<Integer, GameRoom> gameRooms = Collections.emptyMap();

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

		public Builder setLoggedPlayers(final Map<Integer, Player> loggedPlayers) {
			this.loggedPlayers = loggedPlayers;
			return this;
		}

		public Builder setoutEventHandler(final IOutEventHandler outEventHandler) {
			this.outEventHandler = outEventHandler;
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
