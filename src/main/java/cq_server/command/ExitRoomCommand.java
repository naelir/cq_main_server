package cq_server.command;

import java.util.Collections;

import cq_server.event.ExitRoomEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.SepRoom;

public final class ExitRoomCommand extends BaseCommand implements ICommand<ExitRoomEvent> {
	public ExitRoomCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final ExitRoomEvent event, final Player player) {
		final int sepRoomId = player.getWaitState()
			.getSepRoomSel();
		final int roomId = player.getWaitState()
			.getRoomSel();
		final SepRoom room = this.sepRooms.get(sepRoomId);
		if (sepRoomId > 0)
			if (sepRoomId == player.getId()) {
				room.deny();
				this.sepRooms.remove(sepRoomId);
			} else
				room.remove(player);
		if (roomId > 0)
			this.shortRoomPlayers.remove(player);
		player.getWaitState()
			.setSepRoomSel(0);
		player.getWaitState()
			.setRoomSel(0);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run();
	}
}
