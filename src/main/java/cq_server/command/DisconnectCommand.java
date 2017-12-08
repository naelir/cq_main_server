package cq_server.command;

import java.util.Iterator;
import java.util.Map.Entry;

import cq_server.event.DisconnectEvent;
import cq_server.game.Chat;
import cq_server.game.Game;
import cq_server.model.ChatMsg;
import cq_server.model.Page;
import cq_server.model.Player;
import cq_server.model.Player.Type;
import cq_server.model.SepRoom;

public final class DisconnectCommand extends BaseCommand implements ICommand<DisconnectEvent> {
	public DisconnectCommand(final Builder builder) {
		super(builder);
	}

	@Override
	public void execute(final DisconnectEvent event, final Player player) {
		final Page page = player.getPage();
		this.loggedPlayers.remove(player.getId());
		final String name = player.getName();
		switch (page) {
		case GAME:
			final Game game = this.games.remove(player);
			if (game != null) {
				final Player robot = this.playerFactory.createPlayer(Type.ROBOT, null, name);
				this.games.put(robot, game);
				game.disconnect(player, robot);
			}
			break;
		case WAITHALL:
			for (final Iterator<Entry<Integer, Chat>> iterator = this.chats.entrySet()
				.iterator(); iterator.hasNext();) {
				final Chat chat = iterator.next()
					.getValue();
				if (chat.contains(player)) {
					chat.remove(player);
					if (chat.getId() > 0) {
						chat.addMessage(new ChatMsg("#2", name));
						if (chat.isEmpty())
							iterator.remove();
					}
				}
			}
			this.userList.remove(player);
			final int sepRoomId = player.getWaitState()
				.getSepRoomSel();
			if (sepRoomId > 0) {
				final SepRoom room = this.sepRooms.get(sepRoomId);
				room.remove(player);
				if (player.getId() == sepRoomId) {
					room.deny();
					this.sepRooms.remove(sepRoomId);
				}
			}
			this.shortRoomPlayers.remove(player);
			this.waithallRefreshTask.run();
			break;
		default:
			break;
		}
	}
}
