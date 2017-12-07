package cq_server.game;

import cq_server.model.ChatMsg;

public class MainChat extends Chat {
	public MainChat(final int id, final BasePlayer player) {
		super(id, player);
	}

	@Override
	public boolean addMessage(final ChatMsg msg) {
		final String sender = msg.getSender();
		if (sender.equals("#1") || sender.equals("#2"))
			return false;
		else
			return super.addMessage(msg);
	}
}
