package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.ExitRoomEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.SepRoom;;

@SuppressWarnings("unused")
public final class ExitRoomCommand implements ICommand {
	private final ExitRoomEvent event;

	private final Map<Integer, SepRoom> sepRooms;

	private final Deque<BasePlayer> shortRoomPlayers;

	private final AtomicBoolean isWhNeedRefresh;

	private final IOutputMessageHandler outputMessageHandler;

	public ExitRoomCommand(final ExitRoomEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.sepRooms = notNull("sepRooms", builder.sepRooms);
		this.shortRoomPlayers = notNull("shortRoomPlayers", builder.shortRoomPlayers);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final int sepRoomId = player.getWaitState().getSepRoomSel();
		final int roomId = player.getWaitState().getRoomSel();
		final SepRoom room = this.sepRooms.get(sepRoomId);
		if (sepRoomId > 0)
			if (sepRoomId == player.getId()) {
				room.deny();
				this.sepRooms.remove(sepRoomId);
			} else
				room.remove(player);
		if (roomId > 0)
			this.shortRoomPlayers.remove(player);
		player.getWaitState().setSepRoomSel(0);
		player.getWaitState().setRoomSel(0);
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
		this.isWhNeedRefresh.set(true);
	}
}
