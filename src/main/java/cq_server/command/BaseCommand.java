package cq_server.command;

import java.util.Deque;
import java.util.Map;

import cq_server.factory.IPlayerFactory;
import cq_server.factory.IdFactory;
import cq_server.factory.NameFormatter;
import cq_server.game.Chat;
import cq_server.game.Game;
import cq_server.handler.IBanHandler;
import cq_server.handler.IOutEventHandler;
import cq_server.model.Player;
import cq_server.model.SepRoom;
import cq_server.model.UserList;
import cq_server.task.WaithallRefreshTask;

public abstract class BaseCommand {
	protected final Map<Integer, SepRoom> sepRooms;

	protected final IOutEventHandler outEventHandler;

	protected final IPlayerFactory playerFactory;

	protected final WaithallRefreshTask waithallRefreshTask;

	protected final Map<Integer, Player> loggedPlayers;

	protected final NameFormatter nameFormatter;

	protected final Map<Player, Game> games;

	protected final IdFactory idCreator;

	protected final Map<Integer, Chat> chats;

	protected final UserList userList;

	protected final Deque<Player> shortRoomPlayers;

	protected final IBanHandler banHandler;

	public BaseCommand(final Builder builder) {
		this.sepRooms = builder.sepRooms;
		this.outEventHandler = builder.outEventHandler;
		this.playerFactory = builder.playerFactory;
		this.waithallRefreshTask = builder.waithallRefreshTask;
		this.loggedPlayers = builder.loggedPlayers;
		this.nameFormatter = builder.nameFormatter;
		this.games = builder.games;
		this.idCreator = builder.idCreator;
		this.chats = builder.chats;
		this.userList = builder.userList;
		this.shortRoomPlayers = builder.shortRoomPlayers;
		this.banHandler = builder.banHandler;
	}

	public static final class Builder {
		private Map<Integer, SepRoom> sepRooms;

		private IOutEventHandler outEventHandler;

		private IPlayerFactory playerFactory;

		private WaithallRefreshTask waithallRefreshTask;

		private Map<Integer, Player> loggedPlayers;

		private NameFormatter nameFormatter;

		private Map<Player, Game> games;

		private IdFactory idCreator;

		private Map<Integer, Chat> chats;

		private UserList userList;

		private Deque<Player> shortRoomPlayers;

		private IBanHandler banHandler;

		public Builder setBanHandler(final IBanHandler banHandler) {
			this.banHandler = banHandler;
			return this;
		}

		public Builder setChats(final Map<Integer, Chat> chats) {
			this.chats = chats;
			return this;
		}

		public Builder setGames(final Map<Player, Game> games) {
			this.games = games;
			return this;
		}

		public Builder setIdCreator(final IdFactory idCreator) {
			this.idCreator = idCreator;
			return this;
		}

		public Builder setLoggedPlayers(final Map<Integer, Player> loggedPlayers) {
			this.loggedPlayers = loggedPlayers;
			return this;
		}

		public Builder setNameFormatter(final NameFormatter nameFormatter) {
			this.nameFormatter = nameFormatter;
			return this;
		}

		public Builder setOutEventHandler(final IOutEventHandler outEventHandler) {
			this.outEventHandler = outEventHandler;
			return this;
		}

		public Builder setPlayerFactory(final IPlayerFactory playerFactory) {
			this.playerFactory = playerFactory;
			return this;
		}

		public Builder setSepRooms(final Map<Integer, SepRoom> sepRooms) {
			this.sepRooms = sepRooms;
			return this;
		}

		public Builder setShortRoomPlayers(final Deque<Player> shortRoomPlayers) {
			this.shortRoomPlayers = shortRoomPlayers;
			return this;
		}

		public Builder setUserList(final UserList userList) {
			this.userList = userList;
			return this;
		}

		public Builder setWaithallRefreshTask(final WaithallRefreshTask waithallRefreshTask) {
			this.waithallRefreshTask = waithallRefreshTask;
			return this;
		}
	}
}
