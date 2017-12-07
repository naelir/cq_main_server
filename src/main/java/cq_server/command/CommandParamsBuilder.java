package cq_server.command;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.factory.NameFormatter;
import cq_server.game.BasePlayer;
import cq_server.game.Chat;
import cq_server.game.Game;
import cq_server.game.IdFactory;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.SepRoom;
import cq_server.model.UserList;

public class CommandParamsBuilder {
	protected Map<Integer, BasePlayer> loggedPlayers;

	protected Map<Integer, Chat> chats;

	protected UserList usersList;

	protected Map<Integer, SepRoom> sepRooms;

	protected Map<BasePlayer, Game> games;

	protected Deque<BasePlayer> shortRoomPlayers;

	protected IOutputMessageHandler outputMessageHandler;

	protected IdFactory idCreator;

	protected NameFormatter nameFormatter;

	protected AtomicBoolean isWhNeedRefresh;

	public CommandParamsBuilder setChats(final Map<Integer, Chat> chats) {
		this.chats = chats;
		return this;
	}

	public CommandParamsBuilder setGames(final Map<BasePlayer, Game> games) {
		this.games = games;
		return this;
	}

	public CommandParamsBuilder setIdCreator(final IdFactory idCreator) {
		this.idCreator = idCreator;
		return this;
	}

	public CommandParamsBuilder setIsWhNeedRefresh(final AtomicBoolean isWhNeedRefresh) {
		this.isWhNeedRefresh = isWhNeedRefresh;
		return this;
	}

	public CommandParamsBuilder setLoggedPlayers(final Map<Integer, BasePlayer> loggedPlayers) {
		this.loggedPlayers = loggedPlayers;
		return this;
	}

	public CommandParamsBuilder setNameFormatter(final NameFormatter nameFormatter) {
		this.nameFormatter = nameFormatter;
		return this;
	}

	public CommandParamsBuilder setOutputMessageHandler(final IOutputMessageHandler outputMessageHandler) {
		this.outputMessageHandler = outputMessageHandler;
		return this;
	}

	public CommandParamsBuilder setSepRooms(final Map<Integer, SepRoom> sepRooms) {
		this.sepRooms = sepRooms;
		return this;
	}

	public CommandParamsBuilder setShortRoomPlayers(final Deque<BasePlayer> shortRoomPlayers) {
		this.shortRoomPlayers = shortRoomPlayers;
		return this;
	}

	public CommandParamsBuilder setUsersList(final UserList usersList) {
		this.usersList = usersList;
		return this;
	}
}
