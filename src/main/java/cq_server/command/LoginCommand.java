package cq_server.command;

import static cq_server.Assertions.notNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cq_server.event.LoginEvent;
import cq_server.factory.NameFormatter;
import cq_server.game.BasePlayer;
import cq_server.game.Chat;
import cq_server.handler.IOutputMessageHandler;
import cq_server.model.BaseChannel;
import cq_server.model.UserList;;

public final class LoginCommand implements ICommand {
	private final LoginEvent event;

	private final Map<Integer, BasePlayer> loggedPlayers;

	private final Chat mainchat;

	private final UserList usersList;

	private final AtomicBoolean isWhNeedRefresh;

	private final IOutputMessageHandler outputMessageHandler;

	private final NameFormatter nameFormatter;

	public LoginCommand(final LoginEvent event, final CommandParamsBuilder builder) {
		this.event = notNull("event", event);
		this.loggedPlayers = notNull("loggedPlayers", builder.loggedPlayers);
		this.mainchat = notNull("mainchat", builder.chats.get(0));
		this.usersList = notNull("usersList", builder.usersList);
		this.isWhNeedRefresh = notNull("isWhNeedRefresh", builder.isWhNeedRefresh);
		this.outputMessageHandler = notNull("outputMessageHandler", builder.outputMessageHandler);
		this.nameFormatter = notNull("nameFormatter", builder.nameFormatter);
	}

	@Override
	public void execute(final BasePlayer player) {
		final String name = this.event.getUser();
		final BaseChannel cmdChannel = player.getCmdChannel();
		final int id = player.getId();
		final String formattedName = this.nameFormatter.format(name, String.valueOf(id));
		player.setName(formattedName);
		player.setLoggedIn(true);
		this.loggedPlayers.put(id, player);
		this.mainchat.addUser(player);
		this.usersList.add(player);
		this.outputMessageHandler.sendMessage(player, Arrays.asList(cmdChannel));
		this.isWhNeedRefresh.set(true);
	}
}