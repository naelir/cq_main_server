package cq_server.model;

import java.util.Deque;
import java.util.Map;
import java.util.stream.Collectors;

import cq_server.game.Game;

public class ShortGameRoom extends GameRoom {
	private final Map<Player, Game> games;

	private final Deque<Player> shortRoomPlayers;

	public ShortGameRoom(
			final GameRoom.Builder builder,
			final Map<Player, Game> games,
			final Deque<Player> shortRoomPlayers) {
		super(builder);
		this.games = games;
		this.shortRoomPlayers = shortRoomPlayers;
	}

	@Override
	public int getIngame() {
		int ingame = 0;
		for (final Game game : this.games.values().stream().collect(Collectors.toSet()))
			if (game.getType().equals(GameRoomType.SHORT))
				ingame = ingame + Game.GAME_PLAYERS_COUNT;
		return ingame;
	}

	@Override
	public int getWaitingPlayers() {
		return this.shortRoomPlayers.size();
	}
}
