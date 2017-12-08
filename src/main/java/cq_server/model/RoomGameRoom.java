package cq_server.model;

import java.util.Map;
import java.util.stream.Collectors;

import cq_server.game.Game;

public class RoomGameRoom extends GameRoom {
	private final Map<Player, Game> games;

	private final Map<Integer, SepRoom> seprooms;

	public RoomGameRoom(
			final GameRoom.Builder builder,
			final Map<Player, Game> games,
			final Map<Integer, SepRoom> seprooms) {
		super(builder);
		this.games = games;
		this.seprooms = seprooms;
	}

	@Override
	public int getIngame() {
		int ingame = 0;
		for (final Game game : this.games.values().stream().collect(Collectors.toSet()))
			if (game.getType().equals(GameRoomType.ROOM))
				ingame = ingame + Game.GAME_PLAYERS_COUNT;
		return ingame;
	}

	@Override
	public int getWaitingPlayers() {
		int players = 0;
		for (final SepRoom room : this.seprooms.values())
			players = players + room.getPlayers().size();
		return players;
	}
}
