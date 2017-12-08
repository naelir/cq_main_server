package cq_server.task;

import java.util.Queue;

import cq_server.handler.IOutEventHandler;
import cq_server.model.BaseResponse;
import cq_server.model.ChannelEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.Player.Type;

public class OutEventTask implements Runnable, IOutEventHandler {
	private final IOutEventHandler robotHandler;

	private final IOutEventHandler onlinePlayerHandler;

	private final Queue<BaseResponse> outEventQueue;

	public OutEventTask(final Queue<BaseResponse> outEventQueue,
			final IOutEventHandler onlinePlayerHandler, final IOutEventHandler robotHandler) {
		this.outEventQueue = outEventQueue;
		this.onlinePlayerHandler = onlinePlayerHandler;
		this.robotHandler = robotHandler;
	}

	@Override
	public void onChannelEvent(final ChannelEvent event) {
		final Type type = event.getPlayer()
			.getType();
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

	@Override
	public void run() {
		final BaseResponse poll = this.outEventQueue.poll();
		if (poll != null)
			switch (poll.getType()) {
			case CHANNEL:
				this.onChannelEvent((ChannelEvent) poll);
				break;
			case BODY:
				this.onOutEvent((OutEvent) poll);
				break;
			default:
				break;
			}
	}
}