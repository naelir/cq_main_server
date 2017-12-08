package cq_server.factory;

import cq_server.command.*;
import cq_server.model.BaseEvent;
import cq_server.model.Request;

public class DefaultCommandFactory implements ICommandFactory {
	private final BaseCommand.Builder builder;

	public DefaultCommandFactory(final BaseCommand.Builder builder) {
		this.builder = builder;
	}

	@Override
	public ICommand<?> createCommand(final Request<?, ?> request) {
		final BaseEvent event = request.getEvent();
		switch (event.getType()) {
		case ADDSEPROOM:
			return new AddSepRoomCommand(this.builder);
		case ANSWER:
			return new AnswerCommand(this.builder);
		case CHATADDUSER:
			return new ChatAddUserCommand(this.builder);
		case CHATCLOSE:
			return new ChatCloseCommand(this.builder);
		case CHATMESSAGE:
			return new ChatMessageCommand(this.builder);
		case CLOSEGAME:
			return new CloseGameCommand(this.builder);
		case CONNCHECK:
			return new ConnCheckCommand(this.builder);
		case DENYSEPROOM:
			return new DenySepRoomCommand(this.builder);
		case DISCONNECT:
			return new DisconnectCommand(this.builder);
		case ENTERROOM:
			return new EnterRoomCommand(this.builder);
		case EXITROOM:
			return new ExitRoomCommand(this.builder);
		case GETDATA:
			return new GetdataCommand(this.builder);
		case GETUSERINFO:
			return new GetUserInfoCommand(this.builder);
		case LISTEN:
			return new ListenCommand();
		case LOGIN:
			return new LoginCommand(this.builder);
		case LOGOUT:
			return new LogoutCommand(this.builder);
		case MESSAGE:
			return new MessageCommand(this.builder);
		case MODFRIENDLIST:
			return new ModFriendListCommand(this.builder);
		case READY:
			return new ReadyCommand(this.builder);
		case SELECT:
			return new SelectCommand(this.builder);
		case SETACTIVECHAT:
			return new SetActiveChatCommand(this.builder);
		case TIP:
			return new TipCommand(this.builder);
		case WEBLOGIN:
			return new WebLoginCommand(this.builder);
		case ORDERSERVICE:
			return new OrderServiceCommand(this.builder);
		case CHATCOLOR:
			return new ChatColorCommand(this.builder);
		case BADQMARK:
			return new BadQMarkCommand(this.builder);
		case RATEQUESTION:
			return new RateQuestionCommand(this.builder);
		case DEFAULT:
			return new DefaultCommand(this.builder);
		default:
			return new DefaultCommand(this.builder);
		}
	}
}
