package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.DenySepRoomEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.BaseChannel;
import cq_server.model.SepRoom;;

public final class DenySepRoomCommand implements ICommand {
	private final DenySepRoomEvent event;

	private final IOutputMessageHandler outputMessageHandler;

	private final Map<Integer, SepRoom> sepRooms;

	private final AtomicBoolean isWhNeedRefresh;

	public DenySepRoomCommand(final DenySepRoomEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
		this.sepRooms = notNull("sepRooms", builder.sepRooms);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Integer roomId = this.event.getRoom();
		if (roomId.equals(player.getId())) {
			player.getWaitState().setSepRoomSel(0);
			final SepRoom sepRoom = this.sepRooms.get(roomId);
			this.sepRooms.remove(roomId);
			sepRoom.deny();
			this.isWhNeedRefresh.set(true);
		}
		final BaseChannel cmdChannel = player.getCmdChannel();
		this.outputMessageHandler.sendMessage(player, Arrays.asList(cmdChannel));
	}
}
