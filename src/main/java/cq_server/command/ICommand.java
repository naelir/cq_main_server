package cq_server.command;

import cq_server.model.BaseEvent;
import cq_server.model.Player;

public interface ICommand<T extends BaseEvent> {
	void execute(T event, Player player);
}