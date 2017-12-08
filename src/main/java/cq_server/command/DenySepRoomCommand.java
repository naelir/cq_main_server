package cq_server.command;

import java.util.Collections;

import cq_server.event.DenySepRoomEvent;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.SepRoom;

public final class DenySepRoomCommand extends BaseCommand implements ICommand<DenySepRoomEvent> {
	public DenySepRoomCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final DenySepRoomEvent event, final Player player) {
		final Integer roomId = event.getRoom();
		if (roomId.equals(player.getId())) {
			player.getWaitState()
				.setSepRoomSel(0);
			final SepRoom sepRoom = this.sepRooms.get(roomId);
			this.sepRooms.remove(roomId);
			sepRoom.deny();
			this.waithallRefreshTask.run();
		}
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
	}
}
