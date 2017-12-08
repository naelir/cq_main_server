package cq_server.model;

public class Request<C extends BaseChannel, E extends BaseEvent> {
	private final C channel;

	private final E event;

	public Request(final C channel, final E event) {
		super();
		this.channel = channel;
		this.event = event;
	}

	public C getChannel() {
		return this.channel;
	}

	public E getEvent() {
		return this.event;
	}
}
