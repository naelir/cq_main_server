package cq_server.event;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

public final class DisconnectEvent extends BaseEvent {
	public DisconnectEvent() {
		super(EventType.DISCONNECT);
	}
}
