package cq_server.factory;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.event.CmdChannel;
import cq_server.event.DefaultEvent;
import cq_server.model.BaseChannel;
import cq_server.model.BaseEvent;
import cq_server.model.Request;

public class DefaultRequestFactory implements IRequestFactory {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultRequestFactory.class);

	private final JAXBContext cmdContext;

	private final JAXBContext eventContext;

	public DefaultRequestFactory(final JAXBContext cmdContext, final JAXBContext eventContext) {
		this.cmdContext = cmdContext;
		this.eventContext = eventContext;
	}

	private BaseChannel getCmdChannel(final String channel) {
		try {
			return (BaseChannel) this.cmdContext.createUnmarshaller()
				.unmarshal(new StringReader(channel));
		} catch (final JAXBException e) {
			LOG.error("invalid channel", e);
			return new CmdChannel(0, 0, 0);
		}
	}

	private BaseEvent getEvent(final String event) {
		try {
			return (BaseEvent) this.eventContext.createUnmarshaller()
				.unmarshal(new StringReader(event));
		} catch (final JAXBException e) {
			LOG.error("invalid event", e);
			return new DefaultEvent();
		}
	}

	@Override
	public Request<?, ?> getRequest(final String value) {
		final String[] pair = value.split("\r\n");
		if (pair.length == 2) {
			LOG.debug("incoming message: channel: {}, event: {}", pair[0], pair[1]);
			final BaseChannel channel = this.getCmdChannel(pair[0]);
			final BaseEvent event = this.getEvent(pair[1]);
			return new Request<>(channel, event);
		} else
			throw new RuntimeException("invalid message:" + value);
	}
}
