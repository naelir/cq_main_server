package cq_server.factory;

import cq_server.game.BasePlayer;
import cq_server.game.Game;

public interface IPlayerFactory {
	BasePlayer createPlayer(String hostAddress);

	BasePlayer createRobot(Integer id, String name, Game game);
}