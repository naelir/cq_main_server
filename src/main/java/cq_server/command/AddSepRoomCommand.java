package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.AddSeparateRoomEvent;
import cq_server.game.BasePlayer;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.BaseChannel;
import cq_server.model.CountryMap;
import cq_server.model.RoomSettings;
import cq_server.model.SepRoom;

public final class AddSepRoomCommand implements ICommand {
	private final AddSeparateRoomEvent event;

	private final Map<Integer, SepRoom> sepRooms;

	private final AtomicBoolean isWhNeedRefresh;

	private final IOutputMessageHandler outputMessageHandler;

	public AddSepRoomCommand(final AddSeparateRoomEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.sepRooms = notNull("sepRooms", builder.sepRooms);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
	}

	@Override
	public void execute(final BasePlayer player) {
		//@formatter:off
		 final RoomSettings settings = new RoomSettings.Builder()
			.setCountryMap(CountryMap.BG)
			.setOpp1(this.event.getOpp1())
			.setOpp2(this.event.getOpp2())
			.setPersonal(true)
			.setOopp(this.event.getOopp())
			.setRules(this.event.getRules())
			.setSubRules(this.event.getSubRules())
			.setSeppmessageid(0)
			.build(); 
		//@formatter:on
		final SepRoom room = new SepRoom(player, settings);
		final int roomId = room.getId();
		final BaseChannel cmdChannel = player.getCmdChannel();
		player.getWaitState().setSepRoomSel(roomId);
		this.sepRooms.put(roomId, room);
		this.outputMessageHandler.sendMessage(player, Arrays.asList(cmdChannel));
		this.isWhNeedRefresh.set(true);
	}
}
