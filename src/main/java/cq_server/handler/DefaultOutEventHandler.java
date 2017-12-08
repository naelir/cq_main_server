package cq_server.handler;

import cq_server.model.ChannelEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.Player.Type;

public class DefaultOutEventHandler implements IOutEventHandler {
	private final IOutEventHandler robotHandler;

	private final IOutEventHandler onlinePlayerHandler;

	public DefaultOutEventHandler(final IOutEventHandler onlinePlayerHandler, final IOutEventHandler robotHandler) {
		this.onlinePlayerHandler = onlinePlayerHandler;
		this.robotHandler = robotHandler;
	}

	@Override
	public void onChannelEvent(final ChannelEvent event) {
		final Type type = event.getPlayer().getType();
		switch (type) {
		case ONLINE:
			this.onlinePlayerHandler.onChannelEvent(event);
			break;
		case ROBOT:
			this.robotHandler.onChannelEvent(event);
			break;
		default:
			break;
		}
	}

	@Override
	public void onOutEvent(final OutEvent event) {
		final Player player = event.getPlayer();
		final Type type = player.getType();
		switch (type) {
		case ONLINE:
			this.onlinePlayerHandler.onOutEvent(event);
			break;
		case ROBOT:
			this.robotHandler.onOutEvent(event);
			break;
		default:
			break;
		}
	}
}
