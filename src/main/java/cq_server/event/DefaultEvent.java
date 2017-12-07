package cq_server.event;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

public final class DefaultEvent extends BaseEvent {
	public DefaultEvent() {
		super(EventType.DEFAULT);
	}
}
