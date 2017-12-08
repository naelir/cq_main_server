package cq_server.model;

public class PlayerRequest<C extends BaseChannel, E extends BaseEvent> {
	private final Request<C, E> request;

	private final Player player;

	public PlayerRequest(final Request<C, E> request, final Player player) {
		this.request = request;
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Request<C, E> getRequest() {
		return this.request;
	}
}
