package cq_server.factory;

import java.util.List;

import cq_server.game.Game;
import cq_server.model.GameRoomType;
import cq_server.model.Player;

public interface IGameFactory {
	Game createGame(List<Player> players, GameRoomType gameType);
}