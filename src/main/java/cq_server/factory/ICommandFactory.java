package cq_server.factory;

import cq_server.command.ICommand;
import cq_server.model.BaseEvent;

public interface ICommandFactory {
	ICommand createCommand(BaseEvent event);
}