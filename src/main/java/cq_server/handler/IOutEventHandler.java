package cq_server.handler;

import cq_server.model.ChannelEvent;
import cq_server.model.OutEvent;

public interface IOutEventHandler {
	void onChannelEvent(ChannelEvent event);

	void onOutEvent(OutEvent event);
}
