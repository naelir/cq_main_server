package cq_server.model;

public final class NetworkMessage {
	private final BaseChannel channel;

	private final BaseEvent event;

	public NetworkMessage(final BaseChannel channel, final BaseEvent event) {
		super();
		this.channel = channel;
		this.event = event;
	}

	public BaseChannel getChannel() {
		return this.channel;
	}

	public BaseEvent getEvent() {
		return this.event;
	}
}
