package cq_server.test;

import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import cq_server.event.*;
import cq_server.game.Chat;
import cq_server.game.GameState;
import cq_server.game.IdFactory;
import cq_server.game.MockPlayer;
import cq_server.model.*;

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
		marshaller.marshal(new SepRoom(new MockPlayer(1, null, ""), roomSettings), System.out);
		System.out.println();
		final UserList userList = new UserList();
		userList.add(new MockPlayer(1, null, ""));
		userList.add(new MockPlayer(2, null, ""));
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
		tipInfo.setTipInfo(1, 8);
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
