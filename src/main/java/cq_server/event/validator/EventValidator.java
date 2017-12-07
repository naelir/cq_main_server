package cq_server.event.validator;

import static cq_server.Assertions.notNull;

import cq_server.event.AddSeparateRoomEvent;
import cq_server.event.AnswerEvent;
import cq_server.event.ChatAddUserEvent;
import cq_server.event.ChatCloseEvent;
import cq_server.event.ChatMessageEvent;
import cq_server.event.DenySepRoomEvent;
import cq_server.event.GetDataEvent;
import cq_server.event.GetUserInfoEvent;
import cq_server.event.LoginEvent;
import cq_server.event.MessageEvent;
import cq_server.event.SelectAreaEvent;
import cq_server.event.SetActiveChatEvent;
import cq_server.event.TipEvent;
import cq_server.event.WebLoginEvent;
import cq_server.model.BaseEvent;

public class EventValidator implements IEventValidator {
	@Override
	public BaseEvent validate(final BaseEvent baseEvent) {
		switch (baseEvent.getType()) {
		case ADDSEPROOM: {
			final AddSeparateRoomEvent event = (AddSeparateRoomEvent) baseEvent;
			notNull("map", event.getMap());
			notNull("oopp", event.getOopp());
			notNull("opp1", event.getOpp1());
			notNull("opp2", event.getOpp2());
			notNull("rules", event.getRules());
			notNull("seppmessageid", event.getSeppmessageId());
			notNull("subrules", event.getSubRules());
			return event;
		}
		case ANSWER: {
			final AnswerEvent event = (AnswerEvent) baseEvent;
			notNull("answer", event.getAnswer());
			return event;
		}
		case CHATADDUSER: {
			final ChatAddUserEvent event = (ChatAddUserEvent) baseEvent;
			notNull("chatid", event.getChatId());
			notNull("user", event.getUser());
			return event;
		}
		case CHATCLOSE: {
			final ChatCloseEvent event = (ChatCloseEvent) baseEvent;
			notNull("chatid", event.getChatId());
			notNull("user", event.getActiveChat());
			notNull("user", event.getMstate());
			return event;
		}
		case CHATMESSAGE: {
			final ChatMessageEvent event = (ChatMessageEvent) baseEvent;
			notNull("chatid", event.getMsg());
			return event;
		}
		case DENYSEPROOM: {
			final DenySepRoomEvent event = (DenySepRoomEvent) baseEvent;
			notNull("room", event.getRoom());
			return event;
		}
		case GETDATA: {
			final GetDataEvent event = (GetDataEvent) baseEvent;
			notNull("query", event.getQuery());
			return event;
		}
		case GETUSERINFO: {
			final GetUserInfoEvent event = (GetUserInfoEvent) baseEvent;
			notNull("user", event.getUser());
			return event;
		}
		case LOGIN: {
			final LoginEvent event = (LoginEvent) baseEvent;
			notNull("pass", event.getPass());
			notNull("user", event.getUser());
			notNull("whid", event.getWaithallid());
			return event;
		}
		case MESSAGE: {
			final MessageEvent event = (MessageEvent) baseEvent;
			notNull("message", event.getMessage());
			notNull("to", event.getTo());
			return event;
		}
		case SELECT: {
			final SelectAreaEvent event = (SelectAreaEvent) baseEvent;
			notNull("area", event.getArea());
			return event;
		}
		case SETACTIVECHAT: {
			final SetActiveChatEvent event = (SetActiveChatEvent) baseEvent;
			notNull("chatid", event.getChatId());
			notNull("mstate", event.getMstate());
			return event;
		}
		case TIP: {
			final TipEvent event = (TipEvent) baseEvent;
			notNull("user", event.getTip());
			return event;
		}
		case WEBLOGIN: {
			final WebLoginEvent event = (WebLoginEvent) baseEvent;
			notNull("userid", event.getUserid());
			notNull("username", event.getUserName());
			notNull("whid", event.getWaithallid());
			notNull("sessionid", event.getSessionid());
			return event;
		}
		case DEFAULT:
		case READY:
		case MODFRIENDLIST:
		case LOGOUT:
		case CLOSEGAME:
		case CONNCHECK:
		case DISCONNECT:
		case ENTERROOM:
		case EXITROOM:
		case LISTEN: {
			return baseEvent;
		}
		default:
			return baseEvent;
		}
	}
}
