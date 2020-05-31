package cq_server.command;

import java.util.Collections;

import cq_server.event.EnterRoomEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.SepRoom;

public final class EnterRoomCommand extends BaseCommand implements ICommand<EnterRoomEvent> {
	public EnterRoomCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final EnterRoomEvent event, final Player player) {
		final Integer sepRoomId = event.getSepRoomId();
		if (sepRoomId != null) {
			player.getWaitState().setSepRoomSel(sepRoomId);
			final SepRoom room = this.sepRooms.get(sepRoomId);
			room.add(player);
		}
		final Integer roomId = event.getRoomId();
		if (roomId != null) {
			player.getWaitState().setRoomSel(roomId);
			this.shortRoomPlayers.add(player);
		}
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run();
	}
}
