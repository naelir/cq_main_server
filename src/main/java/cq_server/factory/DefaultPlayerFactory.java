package cq_server.factory;

import cq_server.model.Player;
import cq_server.model.Player.Type;

public class DefaultPlayerFactory implements IPlayerFactory {
	private static final String DEFAULT_IP = "localhost";

	private final IdFactory idFactory;

	public DefaultPlayerFactory(final IdFactory idCreator) {
		this.idFactory = idCreator;
	}

	@Override
	public Player createPlayer(final Type type, final String hostAddress, final String name) {
		final int id = this.idFactory.createId(Player.class);
		final String playerName = name != null ? name : String.valueOf(id);
		final String playerHostAddress = hostAddress != null ? hostAddress : DEFAULT_IP;
		return new Player(id, type, playerHostAddress, playerName);
	}
}
