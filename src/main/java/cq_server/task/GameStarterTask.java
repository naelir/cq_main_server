package cq_server.task;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cq_server.factory.IGameFactory;
import cq_server.game.BasePlayer;
import cq_server.game.Chat;
import cq_server.game.Game;
import cq_server.model.GameRoomType;
import cq_server.model.Page;
import cq_server.model.SepRoom;
import cq_server.model.UserList;

public class GameStarterTask implements Runnable {
	private final UserList usersList;

	private final Map<Integer, SepRoom> sepRooms;

	private final Deque<BasePlayer> shortRoomPlayers;

	private final Map<BasePlayer, Game> games;

	private final IGameFactory gameFactory;

	private final Map<Integer, Chat> chats;

	public GameStarterTask(final Builder builder) {
		this.usersList = builder.usersList;
		this.sepRooms = builder.sepRooms;
		this.shortRoomPlayers = builder.shortRoomPlayers;
		this.games = builder.games;
		this.gameFactory = builder.gameFactory;
		this.chats = builder.chats;
	}

	@Override
	public void run() {
		this.startSepRooms();
		this.startShortRooms();
	}

	private boolean startGame(final Set<BasePlayer> players, final GameRoomType type) {
		boolean isAllReady = true;
		for (final BasePlayer basePlayer : players)
			isAllReady = isAllReady && basePlayer.isReady() && basePlayer.isListen();
		if (isAllReady) {
			final Game game = this.gameFactory.createGame(new ArrayList<>(players), type);
			for (final BasePlayer player : players) {
				this.usersList.remove(player);
				for (final Chat chat : this.chats.values())
					chat.remove(player);
				this.games.put(player, game);
				player.setPage(Page.GAME);
			}
			game.tryNextFrame();
			return true;
		} else
			return false;
	}

	private void startSepRooms() {
		for (final Iterator<Entry<Integer, SepRoom>> iterator = this.sepRooms.entrySet().iterator(); iterator
				.hasNext();) {
			final SepRoom room = iterator.next().getValue();
			if (room.isFull())
				if (this.startGame(room.getPlayers(), GameRoomType.ROOM))
					iterator.remove();
		}
	}

	private void startShortRooms() {
		if (this.shortRoomPlayers.size() >= Game.GAME_PLAYERS_COUNT) {
			final Set<BasePlayer> players = this.shortRoomPlayers.stream().limit(Game.GAME_PLAYERS_COUNT)
					.collect(Collectors.toSet());
			if (this.startGame(players, GameRoomType.SHORT))
				this.shortRoomPlayers.removeAll(players);
		}
	}

	public static final class Builder {
		private UserList usersList;

		private Map<Integer, SepRoom> sepRooms;

		private Deque<BasePlayer> shortRoomPlayers;

		private Map<BasePlayer, Game> games;

		private IGameFactory gameFactory;

		private Map<Integer, Chat> chats;

		public GameStarterTask build() {
			return new GameStarterTask(this);
		}

		public Builder setChats(final Map<Integer, Chat> chats) {
			this.chats = chats;
			return this;
		}

		public Builder setGameFactory(final IGameFactory gameFactory) {
			this.gameFactory = gameFactory;
			return this;
		}

		public Builder setGames(final Map<BasePlayer, Game> games) {
			this.games = games;
			return this;
		}

		public Builder setSepRooms(final Map<Integer, SepRoom> sepRooms) {
			this.sepRooms = sepRooms;
			return this;
		}

		public Builder setShortRoomPlayers(final Deque<BasePlayer> shortRoomPlayers) {
			this.shortRoomPlayers = shortRoomPlayers;
			return this;
		}

		public Builder setUsersList(final UserList usersList) {
			this.usersList = usersList;
			return this;
		}
	}
}
