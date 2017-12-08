package cq_server.factory;

import cq_server.command.ICommand;
import cq_server.model.Request;

public interface ICommandFactory {
	ICommand<?> createCommand(Request<?, ?> request);
}