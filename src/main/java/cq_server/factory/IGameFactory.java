package cq_server.factory;

import java.util.List;

import cq_server.game.BasePlayer;
import cq_server.game.Game;
import cq_server.model.GameRoomType;

public interface IGameFactory {
	Game createGame(List<BasePlayer> players, GameRoomType gameType);
}