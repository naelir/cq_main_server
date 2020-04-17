package cq_server.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.shared.utils.StringUtils;

import cq_server.event.AddSeparateRoomEvent;
import cq_server.model.CountryMap;
import cq_server.model.OOPP;
import cq_server.model.OutEvent;
import cq_server.model.Player;
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
        final List<String> invitedNames = new ArrayList<>(3);
        final List<String> acceptedNames = new ArrayList<>(3);
		players.add(player);
		invitedNames.add(player.getName());
		acceptedNames.add(player.getName());
				
		int expectedHumans = 3;
		if (OOPP.HASROBOT.equals(event.getOopp())) {
		    if (StringUtils.isBlank(event.getOpp1())) {
                invitedNames.add("robot-1");
                expectedHumans--;
            } else {
                invitedNames.add(event.getOpp1());
            }
		    
            if (StringUtils.isBlank(event.getOpp2())) {
                invitedNames.add("robot-2");
                expectedHumans--;
            } else {
                invitedNames.add(event.getOpp2());
            }
		} else {
            invitedNames.add(event.getOpp1());
            invitedNames.add(event.getOpp2());
        }

		final SepRoom room = new SepRoom(id, players, invitedNames, acceptedNames, settings, expectedHumans);
		final int roomId = room.getId();
		player.getWaitState()
			.setSepRoomSel(roomId);
		this.sepRooms.put(roomId, room);
		this.outEventHandler.onOutEvent(new OutEvent(OutEvent.Kind.CMD, player, Collections.emptyList()));
		this.waithallRefreshTask.run();
	}
}
