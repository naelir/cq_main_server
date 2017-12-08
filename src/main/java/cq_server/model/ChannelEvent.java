package cq_server.model;

public class ChannelEvent extends BaseResponse {
	private final Player player;

	private final BaseChannel channel;

	public ChannelEvent(final Player player, final BaseChannel channel) {
		super(BaseResponseType.CHANNEL);
		this.player = player;
		this.channel = channel;
	}

	public BaseChannel getChannel() {
		return this.channel;
	}

	public Player getPlayer() {
		return this.player;
	}
}
