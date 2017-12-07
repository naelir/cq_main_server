package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.game.BasePlayer;
import cq_server.game.Chat;
import cq_server.game.Game;
import cq_server.model.ChatMsg;
import cq_server.model.Page;
import cq_server.model.SepRoom;
import cq_server.model.UserList;;

public final class DisconnectCommand implements ICommand {
	private final Map<Integer, BasePlayer> loggedPlayers;

	private final Map<BasePlayer, Game> games;

	private final Map<Integer, Chat> chats;

	private final UserList usersList;

	private final Map<Integer, SepRoom> sepRooms;

	private final Deque<BasePlayer> shortRoomPlayers;

	private final AtomicBoolean isWhNeedRefresh;

	public DisconnectCommand(final CommandParamsBuilder builder) {
		this.loggedPlayers = notNull("loggedPlayers", builder.loggedPlayers);
		this.games = notNull("games", builder.games);
		this.chats = notNull("chats", builder.chats);
		this.usersList = notNull("usersList", builder.usersList);
		this.sepRooms = notNull("sepRooms", builder.sepRooms);
		this.shortRoomPlayers = notNull("shortRoomPlayers", builder.shortRoomPlayers);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
	}

	@Override
	public void execute(final BasePlayer player) {
		final Page page = player.getPage();
		this.loggedPlayers.remove(player.getId());
		switch (page) {
		case GAME:
			final Game game = this.games.remove(player);
			game.disconnect(player);
			break;
		case WAITHALL:
			for (final Iterator<Entry<Integer, Chat>> iterator = this.chats.entrySet().iterator(); iterator
					.hasNext();) {
				final Chat chat = iterator.next().getValue();
				if (chat.contains(player)) {
					chat.remove(player);
					if (chat.getId() > 0) {
						chat.addMessage(new ChatMsg("#2", player.getName()));
						if (chat.isEmpty())
							iterator.remove();
					}
				}
			}
			this.usersList.remove(player);
			final int sepRoomId = player.getWaitState().getSepRoomSel();
			if (sepRoomId > 0) {
				final SepRoom room = this.sepRooms.get(sepRoomId);
				room.remove(player);
				if (player.getId() == sepRoomId) {
					room.deny();
					this.sepRooms.remove(sepRoomId);
				}
			}
			this.shortRoomPlayers.remove(player);
			this.isWhNeedRefresh.set(true);
			break;
		default:
			break;
		}
	}
}
