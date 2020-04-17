package cq_server.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cq_server.command.AddSepRoomCommand;
import cq_server.command.AnswerCommand;
import cq_server.command.BaseCommand;
import cq_server.command.ICommand;
import cq_server.event.AddSeparateRoomEvent;
import cq_server.event.AnswerEvent;
import cq_server.factory.IPlayerFactory;
import cq_server.factory.IQuestionContextFactory;
import cq_server.factory.IdFactory;
import cq_server.factory.NameFormatter;
import cq_server.game.Chat;
import cq_server.game.Game;
import cq_server.game.GameState;
import cq_server.game.MainChat;
import cq_server.handler.IBanHandler;
import cq_server.handler.IOutEventHandler;
import cq_server.model.*;
import cq_server.task.WaithallRefreshTask;

public class CommandsTest {
	private cq_server.command.BaseCommand.Builder builder;

	private Map<Integer, SepRoom> sepRooms;

	private IOutEventHandler outEventHandler;

	private IPlayerFactory playerFactory;

	private WaithallRefreshTask waithallRefreshTask;

	private UserList usersList;

	private NameFormatter nameFormatter;

	private HashMap<Player, Game> games;

	private IdFactory idCreator;

	private Map<Integer, Chat> chats;

	private Deque<Player> shortRoomPlayers;

	private IBanHandler banHandler;

	private Map<Integer, Player> loggedPlayers;

	@Test
	public void addSeparateRoom() {
        this.loggedPlayers.put(1, new Player(1, Player.Type.ROBOT, "", "opponent-1"));
        this.loggedPlayers.put(2, new Player(2, Player.Type.ROBOT, "", "opponent-2"));
		final AddSeparateRoomEvent event = new AddSeparateRoomEvent(0, CountryMap.BG, "opponent-1", "opponent-2",
				OOPP.ANYONE.getValue(), Rules.LONG.getValue(), SubRules.LASTMANSTANDING.getValue(), 0, 0);
		final ICommand<AddSeparateRoomEvent> command = new AddSepRoomCommand(this.builder);
		command.execute(event, new Player(0, Player.Type.ROBOT, "", "test-1"));
		assertThat(this.sepRooms.size()).isEqualTo(1);
	}

	@Test
	public void answer() {
		final AnswerEvent event = new AnswerEvent(4);
		final Player player = new Player(0, Player.Type.ROBOT, "", "test-1");
		final Player player2 = new Player(1, Player.Type.ROBOT, "", "test-2");
		final Player player3 = new Player(2, Player.Type.ROBOT, "", "test-3");
		final LinkedList<QuestionContext> qhistory = new LinkedList<>();
		final QuestionContext questionContext = new QuestionContext(new Question(
				new RawQuestion("no-question", new String[] { "option-1", "option-2", "option-3", "option-4" }, 0),
				new AnswerResult(0, 0, 0, 3)));
		qhistory.add(questionContext);
		final GameHistory gameHistory = new GameHistory(new LinkedList<>(), qhistory, new LinkedList<>());
		final HashMap<Player, Integer> players = new HashMap<>();
		players.put(player, 1);
		players.put(player2, 2);
		players.put(player3, 3);
		final IQuestionContextFactory qContextFactory = mock(IQuestionContextFactory.class);
		final Game game = new Game.Builder().setGameHistory(gameHistory)
			.setId(1)
			.setMessageHandler(this.outEventHandler)
			.setPlayers(players)
			.setQuestionContextFactory(qContextFactory)
			.setState(new GameState())
			.setType(GameRoomType.ROOM)
			.build();
		this.games.put(player, game);
		final AnswerCommand command = new AnswerCommand(this.builder);
		command.execute(event, player);
		final int answer = gameHistory.getQhistory()
			.getLast()
			.getQuestion()
			.getAnswerResult()
			.getP1();
		assertThat(answer).isEqualTo(4);
	}

	@Before
	public void init() {
		this.sepRooms = new HashMap<>();
		this.outEventHandler = mock(IOutEventHandler.class);
		this.playerFactory = mock(IPlayerFactory.class);
		this.loggedPlayers = new HashMap<>();
		this.usersList = new UserList();
		this.nameFormatter = new NameFormatter();
		this.games = new HashMap<>();
		this.idCreator = new IdFactory();
		this.chats = new HashMap<>();
		this.chats.put(0, new MainChat(0, null));
		this.shortRoomPlayers = new ArrayDeque<>();
		this.banHandler = mock(IBanHandler.class);
		this.waithallRefreshTask = new WaithallRefreshTask.Builder().setChats(this.chats)
			.setLoggedPlayers(this.loggedPlayers)
			.setoutEventHandler(this.outEventHandler)
			.setSeparateRooms(this.sepRooms)
			.setUsersList(this.usersList)
			.build();
		this.builder = new BaseCommand.Builder().setBanHandler(this.banHandler)
			.setWaithallRefreshTask(this.waithallRefreshTask)
			.setChats(this.chats)
			.setGames(this.games)
			.setIdCreator(this.idCreator)
			.setLoggedPlayers(this.loggedPlayers)
			.setNameFormatter(this.nameFormatter)
			.setOutEventHandler(this.outEventHandler)
			.setPlayerFactory(this.playerFactory)
			.setSepRooms(this.sepRooms)
			.setShortRoomPlayers(this.shortRoomPlayers)
			.setUserList(this.usersList);
	}
}
