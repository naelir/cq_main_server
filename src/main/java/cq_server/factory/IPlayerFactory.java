package cq_server.factory;

import cq_server.model.Player;
import cq_server.model.Player.Type;

public interface IPlayerFactory {
	Player createPlayer(Type type, String hostAddress, String name);
}