package cq_server.model;

import java.util.List;

public class OutEvent extends BaseResponse {
	private final Player player;

	private final List<Object> messages;

	private final Kind kind;

	public OutEvent(final Kind kind, final Player basePlayer, final List<Object> messages) {
		super(BaseResponseType.BODY);
		this.kind = kind;
		this.player = basePlayer;
		this.messages = messages;
	}

	public Kind getKind() {
		return this.kind;
	}

	public List<Object> getMessages() {
		return this.messages;
	}

	public Player getPlayer() {
		return this.player;
	}

	public enum Kind {
		CMD, LISTEN;
	}
}
