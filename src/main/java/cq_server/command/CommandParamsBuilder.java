package cq_server.command;

import java.util.Deque;
import java.util.Map;

import cq_server.event.CmdChannel;
import cq_server.event.ListenChannel;
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

public class CommandParamsBuilder {
	public Map<Integer, Player> loggedPlayers;

	public Map<Integer, Chat> chats;

	public UserList usersList;

	public Map<Integer, SepRoom> sepRooms;

	public Map<Player, Game> games;

	public Deque<Player> shortRoomPlayers;

	public IOutEventHandler outEventHandler;

	public IdFactory idCreator;

	public NameFormatter nameFormatter;

	public IBanHandler banHandler;

	public IPlayerFactory playerFactory;

	public Runnable waithallRefreshTask;

	public Map<Player, ListenChannel> listenChannels;

	public Map<Player, CmdChannel> cmdChannels;

	public CommandParamsBuilder setBanHandler(final IBanHandler banHandler) {
		this.banHandler = banHandler;
		return this;
	}

	public CommandParamsBuilder setChats(final Map<Integer, Chat> chats) {
		this.chats = chats;
		return this;
	}

	public CommandParamsBuilder setCmdChannels(final Map<Player, CmdChannel> cmdChannels) {
		this.cmdChannels = cmdChannels;
		return this;
	}

	public CommandParamsBuilder setGames(final Map<Player, Game> games) {
		this.games = games;
		return this;
	}

	public CommandParamsBuilder setIdCreator(final IdFactory idCreator) {
		this.idCreator = idCreator;
		return this;
	}

	public CommandParamsBuilder setListenChannels(final Map<Player, ListenChannel> listenChannels) {
		this.listenChannels = listenChannels;
		return this;
	}

	public CommandParamsBuilder setLoggedPlayers(final Map<Integer, Player> loggedPlayers) {
		this.loggedPlayers = loggedPlayers;
		return this;
	}

	public CommandParamsBuilder setNameFormatter(final NameFormatter nameFormatter) {
		this.nameFormatter = nameFormatter;
		return this;
	}

	public CommandParamsBuilder setOutEventHandler(final IOutEventHandler outEventHandler) {
		this.outEventHandler = outEventHandler;
		return this;
	}

	public CommandParamsBuilder setPlayerFactory(final IPlayerFactory playerFactory) {
		this.playerFactory = playerFactory;
		return this;
	}

	public CommandParamsBuilder setSepRooms(final Map<Integer, SepRoom> sepRooms) {
		this.sepRooms = sepRooms;
		return this;
	}

	public CommandParamsBuilder setShortRoomPlayers(final Deque<Player> shortRoomPlayers) {
		this.shortRoomPlayers = shortRoomPlayers;
		return this;
	}

	public CommandParamsBuilder setUsersList(final UserList usersList) {
		this.usersList = usersList;
		return this;
	}

	public CommandParamsBuilder setWaithallRefreshTask(final Runnable waithallRefreshTask) {
		this.waithallRefreshTask = waithallRefreshTask;
		return this;
	}
}
