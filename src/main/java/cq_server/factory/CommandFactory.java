package cq_server.factory;

import cq_server.command.*;
import cq_server.event.*;
import cq_server.model.BaseEvent;

public class CommandFactory implements ICommandFactory {
	private final CommandParamsBuilder builder;

	public CommandFactory(final CommandParamsBuilder builder) {
		this.builder = builder;
	}

	@Override
	public ICommand createCommand(final BaseEvent event) {
		switch (event.getType()) {
		case ADDSEPROOM:
			return new AddSepRoomCommand((AddSeparateRoomEvent) event, this.builder);
		case ANSWER:
			return new AnswerCommand((AnswerEvent) event, this.builder);
		case CHATADDUSER:
			return new ChatAddUserCommand((ChatAddUserEvent) event, this.builder);
		case CHATCLOSE:
			return new ChatCloseCommand((ChatCloseEvent) event, this.builder);
		case CHATMESSAGE:
			return new ChatMessageCommand((ChatMessageEvent) event, this.builder);
		case CLOSEGAME:
			return new CloseGameCommand((CloseGameEvent) event, this.builder);
		case CONNCHECK:
			return new ConnCheckCommand((ConnectionCheckEvent) event, this.builder);
		case DENYSEPROOM:
			return new DenySepRoomCommand((DenySepRoomEvent) event, this.builder);
		case DISCONNECT:
			return new DisconnectCommand(this.builder);
		case ENTERROOM:
			return new EnterRoomCommand((EnterRoomEvent) event, this.builder);
		case EXITROOM:
			return new ExitRoomCommand((ExitRoomEvent) event, this.builder);
		case GETDATA:
			return new GetdataCommand((GetDataEvent) event, this.builder);
		case GETUSERINFO:
			return new GetUserInfoCommand((GetUserInfoEvent) event, this.builder);
		case LISTEN:
			return new ListenCommand((ListenEvent) event, this.builder);
		case LOGIN:
			return new LoginCommand((LoginEvent) event, this.builder);
		case LOGOUT:
			return new LogoutCommand((LogoutEvent) event, this.builder);
		case MESSAGE:
			return new MessageCommand((MessageEvent) event, this.builder);
		case MODFRIENDLIST:
			return new ModFriendListCommand((ModFriendListEvent) event, this.builder);
		case READY:
			return new ReadyCommand((ReadyEvent) event, this.builder);
		case SELECT:
			return new SelectCommand((SelectAreaEvent) event, this.builder);
		case SETACTIVECHAT:
			return new SetActiveChatCommand((SetActiveChatEvent) event, this.builder);
		case TIP:
			return new TipCommand((TipEvent) event, this.builder);
		case WEBLOGIN:
			return new WebLoginCommand((WebLoginEvent) event, this.builder);
		case ORDERSERVICE:
			return new OrderServiceCommand((OrderServiceEvent) event, this.builder);
		case CHATCOLOR:
			return new ChatColorCommand((ChatColorEvent) event, this.builder);
		case DEFAULT:
			return new DefaultCommand((DefaultEvent) event, this.builder);
		default:
			return new DefaultCommand((DefaultEvent) event, this.builder);
		}
	}
}
