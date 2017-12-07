package cq_server.handler;

import java.util.List;

import cq_server.game.BasePlayer;

public interface IOutputMessageHandler {
	boolean ban(BasePlayer player);

	boolean sendMessage(BasePlayer player, List<Object> data);
}
