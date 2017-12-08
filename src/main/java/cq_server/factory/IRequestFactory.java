package cq_server.factory;

import cq_server.model.Request;

public interface IRequestFactory {
	Request<?, ?> getRequest(String value);
}
