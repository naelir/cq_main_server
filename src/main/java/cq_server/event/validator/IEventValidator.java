package cq_server.event.validator;

import cq_server.model.BaseEvent;

public interface IEventValidator {
	BaseEvent validate(BaseEvent baseEvent);
}