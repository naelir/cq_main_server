package cq_server.command;

import cq_server.game.BasePlayer;

public interface ICommand {
	void execute(BasePlayer player);
}