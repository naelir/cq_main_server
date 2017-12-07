package cq_server.model;

public final class NetworkMessage {
	private BaseChannel channel;

	private BaseEvent event;

	public NetworkMessage(BaseChannel channel, BaseEvent event) {
		super();
		this.channel = channel;
		this.event = event;
	}

	public BaseChannel getChannel() {
		return channel;
	}

	public BaseEvent getEvent() {
		return event;
	}
}
