package cq_server.factory;

import java.util.List;

import cq_server.model.NetworkMessage;

public interface IMessageFactory {
	NetworkMessage createInputNetworkMessage(String message);

	String createOutputNetworkMessage(List<Object> data);
}