package cq_server.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.game.Game;

@XmlRootElement(name = "PLAYERS")
public class Players {
	// <PLAYERS PLAYER1="" NOCHAT1="1" PLAYER2="" NOCHAT2="1"
	// PLAYER3="will0thewisp" NOCHAT3="1" YOU="3" OPP1="1" OPP2="2"
	// DIVISION="64" RULES="1" TOURNAMENT="1" />
	private Map<Integer, Player> players;

	@XmlAttribute(name = "PLAYER1")
	private String player1;

	@XmlAttribute(name = "PLAYER2")
	private String player2;

	@XmlAttribute(name = "PLAYER3")
	private String player3;

	@XmlAttribute(name = "YOU")
	private int you;

	@XmlAttribute(name = "OPP1")
	private int opp1;

	@XmlAttribute(name = "OPP2")
	private int opp2;

	@XmlAttribute(name = "DIVISION")
	private int division;

	@XmlAttribute(name = "RULES")
	private int rules;

	public Players() {
		// TODO Auto-generated constructor stub
	}

	public Players(final int you, final Map<Player, Integer> players) {
		assertThat(players.size()).isEqualTo(Game.GAME_PLAYERS_COUNT);
		this.players = new HashMap<>(Game.GAME_PLAYERS_COUNT);
		for (final Map.Entry<Player, Integer> entry : players.entrySet())
			this.players.put(entry.getValue(), entry.getKey());
		this.you = you;
		this.player1 = this.players.get(1)
			.getName();
		this.player2 = this.players.get(2)
			.getName();
		this.player3 = this.players.get(3)
			.getName();
		if (you == 1) {
			this.opp1 = 2;
			this.opp2 = 3;
		}
		if (you == 2) {
			this.opp1 = 1;
			this.opp2 = 3;
		}
		if (you == 3) {
			this.opp1 = 1;
			this.opp2 = 2;
		}
		this.rules = 1;
	}

	@Override
	public String toString() {
		return String.format(
				"<PLAYERS PLAYER1=\"%s\" PLAYER2=\"%s\" PLAYER3=\"%s\" YOU=\"%d\" OPP1=\"%d\" OPP2=\"%d\" DIVISION=\"0\" RULES=\"1\" SUBRULES=\"2\" />",
				this.players.get(0)
					.getName(),
				this.players.get(1)
					.getName(),
				this.players.get(2)
					.getName(),
				this.you, this.opp1, this.opp2);
	}
}
