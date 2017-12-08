package cq_server.model;

public abstract class BaseEvent {
	private final EventType type;

	public BaseEvent(final EventType type) {
		super();
		this.type = type;
	}

	public EventType getType() {
		return this.type;
	}
}
