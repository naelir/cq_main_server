package cq_server.test;

import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import cq_server.event.AddSeparateRoomEvent;
import cq_server.event.AnswerEvent;
import cq_server.event.ChatAddUserEvent;
import cq_server.event.ChatCloseEvent;
import cq_server.event.ChatMessageEvent;
import cq_server.event.CloseGameEvent;
import cq_server.event.CmdChannel;
import cq_server.event.ConnectionCheckEvent;
import cq_server.event.DenySepRoomEvent;
import cq_server.event.EnterRoomEvent;
import cq_server.event.ExitRoomEvent;
import cq_server.event.GetDataEvent;
import cq_server.event.GetUserInfoEvent;
import cq_server.event.ListenChannel;
import cq_server.event.ListenEvent;
import cq_server.event.LoginEvent;
import cq_server.event.LogoutEvent;
import cq_server.event.MessageEvent;
import cq_server.event.ModFriendListEvent;
import cq_server.event.RateQuestionEvent;
import cq_server.event.ReadyEvent;
import cq_server.event.SelectAreaEvent;
import cq_server.event.SetActiveChatEvent;
import cq_server.event.TipEvent;
import cq_server.event.WebLoginEvent;
import cq_server.factory.IdFactory;
import cq_server.game.Chat;
import cq_server.game.GameState;
import cq_server.model.ActiveChat;
import cq_server.model.AnswerResult;
import cq_server.model.BaseChannel;
import cq_server.model.ChatMsg;
import cq_server.model.CmdAnswer;
import cq_server.model.CmdSelect;
import cq_server.model.CmdTip;
import cq_server.model.ConnStatus;
import cq_server.model.CountryMap;
import cq_server.model.FriendList;
import cq_server.model.FriendListChange;
import cq_server.model.GameOver;
import cq_server.model.GameRoom;
import cq_server.model.GameRoomType;
import cq_server.model.Message;
import cq_server.model.MyData;
import cq_server.model.NewMailFlag;
import cq_server.model.NewReg;
import cq_server.model.NoChat;
import cq_server.model.OOPP;
import cq_server.model.Player;
import cq_server.model.Player.Type;
import cq_server.model.Players;
import cq_server.model.Question;
import cq_server.model.RawQuestion;
import cq_server.model.Reconnect;
import cq_server.model.Rights;
import cq_server.model.RoomSettings;
import cq_server.model.Rules;
import cq_server.model.SepRoom;
import cq_server.model.ShortGameRoom;
import cq_server.model.Substitute;
import cq_server.model.TipInfo;
import cq_server.model.TipQuestion;
import cq_server.model.TipResult;
import cq_server.model.UserInfo;
import cq_server.model.UserList;
import cq_server.model.WaitState;
import cq_server.model.WinnerSelect;

public class JaxbTest {
	@Test
	public void testjaxb() throws JAXBException {
		final Class<?>[] outConvertableClasses = new Class<?>[] {
			//@formatter:off
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
			WinnerSelect.class,
			RateQuestionEvent.class
			//@formatter:on
		};
		final JAXBContext context = JAXBContext.newInstance(outConvertableClasses);
		final Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//@formatter:off
		final GameRoom.Builder roomGameRoomBuilder = new GameRoom.Builder();
		roomGameRoomBuilder
			.setId(1)
			.setTitle("Room")
			.setMinClp(0)
			.setType(GameRoomType.SHORT)
			.setMap(CountryMap.BG);
		//@formatter:on
		marshaller.marshal(new ShortGameRoom(roomGameRoomBuilder, new HashMap<>(), new ArrayDeque<>()), System.out);
		System.out.println();
		marshaller.marshal(new Message(0, 0, "message"), System.out);
		System.out.println();
		marshaller.marshal(new ChatMsg("sender", "message"), System.out);
		System.out.println();
		final int chatId = new IdFactory().createId(Chat.class);
		marshaller.marshal(new Chat(chatId, null), System.out);
		System.out.println();
		marshaller.marshal(new FriendListChange("+Big Soul"), System.out);
		System.out.println();
		//@formatter:off
		final RoomSettings.Builder builder = new RoomSettings.Builder();
		final RoomSettings roomSettings = 
			builder
			.setCountryMap(CountryMap.BG)
			.setCreator("creator")
			.setOopp(OOPP.ANYONE)
			.setOpp1("opp1")
			.setRules(Rules.DUEL)
			.build(); 
		//@formatter:on
        marshaller.marshal(new SepRoom(1, Collections.emptyList(), Arrays.asList("1", "2", "3"),
                Collections.emptyList(), roomSettings, 3), System.out);
		System.out.println();
		final UserList userList = new UserList();
		userList.add(new Player(1, Type.ROBOT, null, ""));
		userList.add(new Player(2, Type.ROBOT, null, ""));
		marshaller.marshal(userList, System.out);
		System.out.println();
		marshaller.marshal(new CmdAnswer(), System.out);
		marshaller.marshal(new CmdTip(), System.out);
		marshaller.marshal(new CmdSelect(), System.out);
		System.out.println();
		final AnswerResult answerResult = new AnswerResult(9, 1, 2, 2);
		answerResult.setAnswer(1, 89);
		answerResult.setAnswer(2, 8);
		marshaller.marshal(answerResult, System.out);
		System.out.println();
		marshaller.marshal(
				new Question(new RawQuestion("", new String[] { "a", "b", "c", "d" }, 2), new AnswerResult()),
				System.out);
		System.out.println();
		final TipInfo tipInfo = new TipInfo(0, 2);
		tipInfo.setTip(1, 8);
		marshaller.marshal(tipInfo, System.out);
		System.out.println();
		marshaller.marshal(new GameState(), System.out);
		System.out.println();
		final Class<?>[] inEventClasses = new Class<?>[] {
				//@formatter:off 
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
				WebLoginEvent.class
				//@formatter:on
		};
		final Unmarshaller unmarshaller = JAXBContext.newInstance(inEventClasses).createUnmarshaller();
		final Object object = unmarshaller.unmarshal(new StringReader(
				"<ADDSEPROOM MAP=\"FR\" OPP2=\"\" OOPP=\"2\" RULES=\"2\" SEPMESSAGEID=\"0\" QCATS=\"7\" SUBRULES=\"2\" />"));
		System.out.println(object);
		final ChatMsg chatMsg = new ChatMsg("", "");
		marshaller.marshal(chatMsg, System.out);
	}
}
