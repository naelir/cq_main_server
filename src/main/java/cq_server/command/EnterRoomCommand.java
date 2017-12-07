package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.EnterRoomEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.SepRoom;;

public final class EnterRoomCommand implements ICommand {
	private final EnterRoomEvent event;

	private final Map<Integer, SepRoom> sepRooms;

	private final Deque<BasePlayer> shortRoomPlayers;

	private final IOutputMessageHandler outputMessageHandler;

	private final AtomicBoolean isWhNeedRefresh;

	public EnterRoomCommand(final EnterRoomEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.sepRooms = notNull("sepRooms", builder.sepRooms);
		this.shortRoomPlayers = notNull("shortRoomPlayers", builder.shortRoomPlayers);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Integer sepRoomId = this.event.getSepRoomId();
		if (sepRoomId != null) {
			player.getWaitState().setSepRoomSel(sepRoomId);
			final SepRoom room = this.sepRooms.get(sepRoomId);
			room.add(player);
		}
		final Integer roomId = this.event.getRoomId();
		if (roomId != null) {
			player.getWaitState().setRoomSel(roomId);
			this.shortRoomPlayers.add(player);
		}
		this.outputMessageHandler.sendMessage(player, Arrays.asList(player.getCmdChannel()));
		this.isWhNeedRefresh.set(true);
	}
}
