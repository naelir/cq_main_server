package cq_server.task;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.maven.shared.utils.StringUtils;

import cq_server.factory.IGameFactory;
import cq_server.factory.IPlayerFactory;
import cq_server.game.Chat;
import cq_server.game.Game;
import cq_server.model.GameRoomType;
import cq_server.model.OOPP;
import cq_server.model.Page;
import cq_server.model.Player;
import cq_server.model.SepRoom;
import cq_server.model.UserList;
import cq_server.model.Player.Type;

public class GameStarterTask implements Runnable {
	private final UserList usersList;

	private final Map<Integer, SepRoom> sepRooms;

	private final Deque<Player> shortRoomPlayers;

	private final Map<Player, Game> games;

	private final IGameFactory gameFactory;

	private final Map<Integer, Chat> chats;

    private final IPlayerFactory playerFactory;

	public GameStarterTask(final Builder builder) {
		this.usersList = builder.usersList;
		this.sepRooms = builder.sepRooms;
		this.shortRoomPlayers = builder.shortRoomPlayers;
		this.games = builder.games;
		this.gameFactory = builder.gameFactory;
		this.chats = builder.chats;
		this.playerFactory = builder.playerFactory;
	}

	@Override
	public void run() {
		this.startSepRooms();
		this.startShortRooms();
	}

	private void startGame(final Set<Player> players, final GameRoomType type) {
		final Game game = this.gameFactory.createGame(new ArrayList<>(players), type);
		for (final Player player : players) {
			this.usersList.remove(player);
			for (final Chat chat : this.chats.values())
				chat.remove(player);
			this.games.put(player, game);
			player.setPage(Page.GAME);
		}
		game.start();
	}

	private void startSepRooms() {
		for (final Iterator<Entry<Integer, SepRoom>> iterator = this.sepRooms.entrySet().iterator(); iterator
				.hasNext();) {
			final SepRoom room = iterator.next().getValue();
			Set<Player> players = room.getPlayers();
			if (room.isFull()) {

		        if (OOPP.HASROBOT.getValue() == room.getOopp()) {
		            if (StringUtils.isBlank(room.getU2())) {
		                final Player robot = this.playerFactory.createPlayer(Type.ROBOT, null,
		                        String.format("robot-%d", 1));
		                players.add(robot);
		            }
                    if (StringUtils.isBlank(room.getU3())) {
		                final Player robot = this.playerFactory.createPlayer(Type.ROBOT, null,
		                        String.format("robot-%d", 2));
		                players.add(robot);
		            }
		        }
				iterator.remove();
				this.startGame(players, GameRoomType.ROOM);
			}
		}
	}

	private void startShortRooms() {
		if (this.shortRoomPlayers.size() >= Game.GAME_PLAYERS_COUNT) {
			final Set<Player> players = this.shortRoomPlayers.stream().limit(Game.GAME_PLAYERS_COUNT)
					.collect(Collectors.toSet());
			this.startGame(players, GameRoomType.SHORT);
			this.shortRoomPlayers.removeAll(players);
		}
	}

	public static final class Builder {
		private UserList usersList;

		private Map<Integer, SepRoom> sepRooms;

		private Deque<Player> shortRoomPlayers;

		private Map<Player, Game> games;

		private IGameFactory gameFactory;

		private Map<Integer, Chat> chats;

        private IPlayerFactory playerFactory;

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

		public Builder setGames(final Map<Player, Game> games) {
			this.games = games;
			return this;
		}

		public Builder setSepRooms(final Map<Integer, SepRoom> sepRooms) {
			this.sepRooms = sepRooms;
			return this;
		}

		public Builder setShortRoomPlayers(final Deque<Player> shortRoomPlayers) {
			this.shortRoomPlayers = shortRoomPlayers;
			return this;
		}

		public Builder setUsersList(final UserList usersList) {
			this.usersList = usersList;
			return this;
		}

        public Builder setPlayerfactory(final IPlayerFactory playerFactory) {            
            this.playerFactory = playerFactory;
            return this;
        }
	}
}
