package cq_server.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import cq_server.event.AddSeparateRoomEvent;
import cq_server.model.CountryMap;
import cq_server.model.OOPP;
import cq_server.model.OutEvent;
import cq_server.model.Player;
import cq_server.model.Player.Type;
import cq_server.model.RoomSettings;
import cq_server.model.SepRoom;

public final class AddSepRoomCommand extends BaseCommand implements ICommand<AddSeparateRoomEvent> {
	public AddSepRoomCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final AddSeparateRoomEvent event, final Player player) {
		final int id = player.getId();
		final RoomSettings settings = new RoomSettings.Builder().setCountryMap(CountryMap.BG)
			.setOpp1(event.getOpp1())
			.setOpp2(event.getOpp2())
			.setPersonal(true)
			.setOopp(event.getOopp())
			.setRules(event.getRules())
			.setSubRules(event.getSubRules())
			.setSeppmessageid(0)
			.build();
		final List<Player> players = new ArrayList<>(3);
		players.add(player);
		if (event.getOopp()
			.equals(OOPP.HASROBOT))
			for (int i = 0; i < 2; i++) {
				final Player robot = this.playerFactory.createPlayer(Type.ROBOT, null,
						String.format("robot-%d", i + 1));
				players.add(robot);
			}
		final List<String> names = players.stream()
			.map(element -> element.getName())
			.collect(Collectors.toList());
		final SepRoom room = new SepRoom(id, players, names, settings);
		final int roomId = room.getId();
		player.getWaitState()
			.setSepRoomSel(roomId);
		this.sepRooms.put(roomId, room);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run();
	}
}
