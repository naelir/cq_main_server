package cq_server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

import cq_server.command.BaseCommand;
import cq_server.event.*;
import cq_server.factory.*;
import cq_server.game.Chat;
import cq_server.game.Game;
import cq_server.game.GameState;
import cq_server.game.MainChat;
import cq_server.handler.*;
import cq_server.http.IpFilter;
import cq_server.http.IpLimitFilter;
import cq_server.http.IpTimeWindowManager;
import cq_server.model.*;
import cq_server.task.GameStarterTask;
import cq_server.task.QuestionRefreherTask;
import cq_server.task.WaithallRefreshTask;

//@formatter:off
@SpringBootApplication
public class Application {
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private static final String BANNED_IPS_FILE_NAME = "banned-ips.txt";

	private static final String CMD_ARG_NAME_PREFIX = "--";

	private static final String Q_ENDPOINT = "--qEndpoint";

	private static final String T_ENDPOINT = "--tEndpoint";

	private static final int SCHEDULER_THREAD_COUNT = 3;

	private static final int FLASH_TCP_PORT = 2002;

	private static final int SERVER_TIMEOUT_MILLIS = 20000;

	private static final int SHORT_GAMES_GAMEROOM_ID = 1;

	private static final int ROOMS_GAMEROOM_ID = 3;

	private static Map<String, String> argsToMap(final String[] args, final String prefix) {
		final Map<String, String> map = new HashMap<>();
		for (int i = 0; i < args.length; i++)
			if (args[i].startsWith(prefix))
				map.put(args[i], args[i + 1]);
		return map;
	}

 	@SuppressWarnings("unchecked")
	public static void main(final String[] args) {
		final Class<Application> mainClass = Application.class;
		final SpringApplication application = new SpringApplication(mainClass);
		application.setBannerMode(Banner.Mode.OFF);
		application.setWebEnvironment(true);
		final ConfigurableApplicationContext context = application.run(args);
		LOG.info("cq server starting");
		try {
			final Map<String, String> argsMap = argsToMap(args, CMD_ARG_NAME_PREFIX);
			final String questionsApiEndpoint = argsMap.get(Q_ENDPOINT);
			final String tipsApiEndpoint = argsMap.get(T_ENDPOINT);
			final Class<?>[] inCmdClasses = new Class<?>[] {
				BaseChannel.class,
				ListenChannel.class,
				CmdChannel.class
			};
			final Class<?>[] inEventClasses = new Class<?>[] {
				AddSeparateRoomEvent.class,
				AnswerEvent.class,
				ChatCloseEvent.class,
				ChatAddUserEvent.class,
				ChatMessageEvent.class,
				CloseGameEvent.class,
				ConnectionCheckEvent.class,
				DenySepRoomEvent.class,
				EnterRoomEvent.class,
				ExitRoomEvent.class,
				GetDataEvent.class,
				GetUserInfoEvent.class,
				ListenEvent.class,
				LoginEvent.class,
				LogoutEvent.class,
				MessageEvent.class,
				ModFriendListEvent.class,
				ReadyEvent.class,
				SelectAreaEvent.class,
				SetActiveChatEvent.class,
				TipEvent.class,
				WebLoginEvent.class,
				ChatColorEvent.class,
				OrderServiceEvent.class,
				BadQMarkEvent.class,
				RateQuestionEvent.class
			};
			final Class<?>[] outConvertableClasses = new Class<?>[] {
				ActiveChat.class ,
				AnswerResult.class,
				CmdAnswer.class,
				CmdSelect.class,
				CmdTip.class,
				BaseChannel.class,
				ListenChannel.class,
				CmdChannel.class,
				Chat.class,
				ChatMsg.class,
				ConnStatus.class,
				FriendList.class,
				FriendListChange.class,
				GameOver.class,
				GameRoom.class,
				GameState.class,
				Message.class,
				MyData.class,
				NewMailFlag.class,
				NewReg.class,
				NoChat.class,
				Players.class,
				Question.class,
				Reconnect.class,
				Rights.class,
				SepRoom.class,
				Substitute.class,
				TipInfo.class,
				TipQuestion.class,
				TipResult.class,
				UserInfo.class,
				UserList.class,
				WaitState.class,
				WinnerSelect.class
			};
			final Map<Integer, List<Integer>> mapBgNeighbors = new HashMap<>();
			mapBgNeighbors.put(1, Arrays.asList(2, 7, 8));
			mapBgNeighbors.put(2, Arrays.asList(1, 3, 8));
			mapBgNeighbors.put(3, Arrays.asList(2, 4, 8, 9));
			mapBgNeighbors.put(4, Arrays.asList(3, 5, 9, 10));
			mapBgNeighbors.put(5, Arrays.asList(4, 6, 10));
			mapBgNeighbors.put(6, Arrays.asList(5, 10, 15));
			mapBgNeighbors.put(7, Arrays.asList(1, 8, 11));
			mapBgNeighbors.put(8, Arrays.asList(1, 2, 3, 7, 9, 11, 12, 13));
			mapBgNeighbors.put(9, Arrays.asList(3, 4, 8, 10, 13, 14, 15));
			mapBgNeighbors.put(10, Arrays.asList(4, 5, 6, 9, 15));
			mapBgNeighbors.put(11, Arrays.asList(7, 8, 12, 16, 17));
			mapBgNeighbors.put(12, Arrays.asList(8, 11, 13, 16, 17, 18));
			mapBgNeighbors.put(13, Arrays.asList(8, 9, 12, 14, 18));
			mapBgNeighbors.put(14, Arrays.asList(9, 13, 15, 18));
			mapBgNeighbors.put(15, Arrays.asList(6, 9, 10, 14));
			mapBgNeighbors.put(16, Arrays.asList(11, 17));
			mapBgNeighbors.put(17, Arrays.asList(11, 12, 16, 18));
			mapBgNeighbors.put(18, Arrays.asList(12, 13, 14, 17));
			final List<String> bannedIps = context.getBean("bannedIps", List.class);
			final List<String> ips = new BannedIpsLoader().load(BANNED_IPS_FILE_NAME);
			bannedIps.addAll(ips);
			final List<RawQuestion> rawQuestions = new ArrayList<>();
			final List<RawTip> rawTips = new ArrayList<>();
			final Map<Integer, Chat> chats = new ConcurrentHashMap<>();
			final Map<Integer, GameRoom> gameRooms = new HashMap<>();
			final Map<Player, Game> games = new ConcurrentHashMap<>();
			final IdFactory idFactory = new IdFactory();
			final Map<Integer, Player> loggedPlayers = new ConcurrentHashMap<>();
			final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_THREAD_COUNT, r -> new Thread(r, "scheduler-thread"));
			final Map<Integer, SepRoom> sepRooms = new ConcurrentHashMap<>();
			final Deque<Player> shortRoomPlayers = new ConcurrentLinkedDeque<>();
			final JAXBContext cmdContext = JAXBContext.newInstance(inCmdClasses);
			final JAXBContext eventContext = JAXBContext.newInstance(inEventClasses);
			final JAXBContext outMessagesContext = JAXBContext.newInstance(outConvertableClasses);
			final Map<INonBlockingConnection, Player> connectedPlayers = new ConcurrentHashMap<>();
			final Map<Player, INonBlockingConnection> connections = new ConcurrentHashMap<>();
			final UserList usersList = new UserList();
 			final IResponseFactory responseFactory = new DefaultResponseFactory(outMessagesContext);
			final IBanHandler banHandler = new DefaultBanHandler(connections, bannedIps);
			final IOutEventHandler robotHandler = new RobotHandler(games, scheduler);
			final Map<Player, Queue<OutEvent>> toSendMessages = new ConcurrentHashMap<>();
			final IOutEventHandler onlinePlayerHandler = new OnlinePlayerHandler(connections, responseFactory, toSendMessages);
			final IOutEventHandler outEventHandler = new DefaultOutEventHandler(onlinePlayerHandler, robotHandler);
			final NameFormatter nameFormatter = new NameFormatter();
 			final WaithallRefreshTask  waithallRefreshTask = new WaithallRefreshTask.Builder()
 					.setSeparateRooms(sepRooms)
					.setChats(chats)
					.setGameRooms(gameRooms)
					.setLoggedPlayers(loggedPlayers)
					.setoutEventHandler(outEventHandler)
					.setUsersList(usersList)
					.build();
			final IPlayerFactory playerFactory = new DefaultPlayerFactory(idFactory);
			final BaseCommand.Builder commandParamsBuilder = new BaseCommand.Builder()
					.setChats(chats)
					.setGames(games)
					.setIdCreator(idFactory)
					.setLoggedPlayers(loggedPlayers)
					.setOutEventHandler(outEventHandler)
					.setSepRooms(sepRooms)
					.setNameFormatter(nameFormatter)
					.setUserList(usersList)
					.setShortRoomPlayers(shortRoomPlayers)
 					.setBanHandler(banHandler)
					.setPlayerFactory(playerFactory)
					.setWaithallRefreshTask(waithallRefreshTask);
			final ICommandFactory commandFactory = new DefaultCommandFactory(commandParamsBuilder);
			final IQuestionFactory questionFactory = new QuestionFactory(rawQuestions, rawTips);
			final IQuestionContextFactory questionContextFactory = new QuestionContextFactory(questionFactory);
			final IGameFactory gameFactory = new DefaultGameFactory(idFactory, questionContextFactory,  mapBgNeighbors, outEventHandler);
 			final IRequestFactory requestFactory = new DefaultRequestFactory(cmdContext, eventContext)
;			final IDataHandler dataHandler = new OnDataHandler.Builder()
					.setCommandFactory(commandFactory)
					.setConnectedPlayers(connectedPlayers)
					.setRequestFactory(requestFactory)
 					.setOutEventHandler(outEventHandler)
					.build();
			final IConnectHandler connectHandler = new OnConnectHandler.Builder()
					.setBannedIps(bannedIps)
					.setConnectedPlayers(connectedPlayers)
					.setConnections(connections)
					.setPlayerFactory(playerFactory)
					.setToSendMessages(toSendMessages)
					.build();
			final IDisconnectHandler disconnectHandler = new OnDisconnectHandler.Builder()
					.setCommandFactory(commandFactory)
					.setConnectedPlayers(connectedPlayers)
					.setConnections(connections)
					.setToSendMessages(toSendMessages)
					.build();

			final IInEventHandler inputMessageHandler = new DefaultInEventHandler(dataHandler, disconnectHandler, connectHandler);
 			final Runnable gameStarterTask = new GameStarterTask.Builder()
					.setChats(chats)
					.setGameFactory(gameFactory)
					.setGames(games)
					.setSepRooms(sepRooms)
					.setShortRoomPlayers(shortRoomPlayers)
					.setUsersList(usersList)
					.build();
 			final IQuestionsLoader questionsLoader = new OnlineQuestionsLoader(questionsApiEndpoint, tipsApiEndpoint);
			final Runnable questionRefreherTask = new QuestionRefreherTask(rawQuestions, rawTips, questionsLoader);
			final GameRoom.Builder shortGameRoomBuilder = new GameRoom.Builder()
					.setId(SHORT_GAMES_GAMEROOM_ID)
					.setTitle("Кратка състезателна")
					.setMinClp(0)
					.setType(GameRoomType.SHORT)
					.setMap(CountryMap.BG);
			final ShortGameRoom shortGameRoom = new ShortGameRoom(shortGameRoomBuilder, games, shortRoomPlayers);
			final GameRoom.Builder roomGameRoomBuilder = new GameRoom.Builder()
					.setId(ROOMS_GAMEROOM_ID)
					.setTitle("Room")
					.setMinClp(0)
					.setType(GameRoomType.ROOM)
					.setMap(CountryMap.BG);
			final RoomGameRoom roomGameRoom = new RoomGameRoom(roomGameRoomBuilder, games, sepRooms);
			final IServer server = new Server(FLASH_TCP_PORT, inputMessageHandler);
			server.setIdleTimeoutMillis(SERVER_TIMEOUT_MILLIS);
			gameRooms.put(SHORT_GAMES_GAMEROOM_ID, shortGameRoom);
			gameRooms.put(ROOMS_GAMEROOM_ID, roomGameRoom);
			final int mainChatid = idFactory.createId(Chat.class);
			final Chat mainChat = new MainChat(mainChatid, null);
			chats.put(mainChat.getId(), mainChat);
 			scheduler.scheduleAtFixedRate(gameStarterTask, 0, 200, TimeUnit.MILLISECONDS);
  			scheduler.scheduleAtFixedRate(questionRefreherTask, 0, 6, TimeUnit.HOURS);
			new Thread(server, "cq-server").start();
		} catch (final IOException | JAXBException e) {
			LOG.error("unexpected error occurred", e);
		}
	}

	private final Builder buidler;

	public Application() {
		final List<String> allowedIps = new ArrayList<>();
		final List<String> bannedIps = new ArrayList<>();
		final IpFilter ipFilter = new IpFilter(bannedIps, allowedIps);
		final FilterRegistrationBean ipFilterRegistrationBean = new FilterRegistrationBean();
		ipFilterRegistrationBean.setFilter(ipFilter);
		ipFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		final IpTimeWindowManager ipTimeWindowManager = new IpTimeWindowManager();
		final IpLimitFilter ipLimitFilter = new IpLimitFilter(ipTimeWindowManager);
		final FilterRegistrationBean ipLimitFilterRegistrationBean = new FilterRegistrationBean();
		ipLimitFilterRegistrationBean.setFilter(ipLimitFilter);
		ipLimitFilterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		this.buidler =
				new Builder()
				.setAllowedIps(allowedIps)
				.setBannedIps(bannedIps).setIpFilter(ipFilter)
				.setIpFilterRegistrationBean(ipFilterRegistrationBean)
				.setIpLimitFilter(ipLimitFilter)
				.setIpLimitFilterRegistrationBean(ipLimitFilterRegistrationBean)
				.setIpTimeWindowManager(ipTimeWindowManager);
	}

	@Bean(name = "allowedIps")
	public List<String> allowedIps() {
		return this.buidler.allowedIps;
	}

	@Bean(name = "bannedIps")
	public List<String> bannedIps() {
		return this.buidler.bannedIps;
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public IpFilter getIpFilter() {
		return this.buidler.ipFilter;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public FilterRegistrationBean ipFilterRegistrationBean() {
		return this.buidler.ipFilterRegistrationBean;
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public IpLimitFilter ipLimitFilter() {
		return this.buidler.ipLimitFilter;
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public FilterRegistrationBean ipLimitFilterRegistrationBean() {
		return this.buidler.ipLimitFilterRegistrationBean;
	}

	@Bean
	@Scope(value = "singleton")
	@Order(Ordered.LOWEST_PRECEDENCE)
	public IpTimeWindowManager ipTimeWindowManager() {
		return this.buidler.ipTimeWindowManager;
	}

	public static final class Builder {
		List<String> allowedIps;

		List<String> bannedIps;

		IpFilter ipFilter;

		FilterRegistrationBean ipFilterRegistrationBean;

		IpLimitFilter ipLimitFilter;

		FilterRegistrationBean ipLimitFilterRegistrationBean;

		IpTimeWindowManager ipTimeWindowManager;

		public Builder setAllowedIps(final List<String> allowedIps) {
			this.allowedIps = allowedIps;
			return this;
		}

		public Builder setBannedIps(final List<String> bannedIps) {
			this.bannedIps = bannedIps;
			return this;
		}

		public Builder setIpFilter(final IpFilter ipFilter) {
			this.ipFilter = ipFilter;
			return this;
		}

		public Builder setIpFilterRegistrationBean(final FilterRegistrationBean ipFilterRegistrationBean) {
			this.ipFilterRegistrationBean = ipFilterRegistrationBean;
			return this;
		}

		public Builder setIpLimitFilter(final IpLimitFilter ipLimitFilter) {
			this.ipLimitFilter = ipLimitFilter;
			return this;
		}

		public Builder setIpLimitFilterRegistrationBean(final FilterRegistrationBean ipLimitFilterRegistrationBean) {
			this.ipLimitFilterRegistrationBean = ipLimitFilterRegistrationBean;
			return this;
		}

		public Builder setIpTimeWindowManager(final IpTimeWindowManager ipTimeWindowManager) {
			this.ipTimeWindowManager = ipTimeWindowManager;
			return this;
		}
	}
}