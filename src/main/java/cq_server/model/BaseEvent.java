package cq_server.model;

public abstract class BaseEvent {
	private EventType type;

	public BaseEvent(EventType type) {
		super();
		this.type = type;
	}

	public EventType getType() {
		return type;
	}
}
