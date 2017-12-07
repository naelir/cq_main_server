package cq_server.factory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_server.event.CmdChannel;
import cq_server.event.DefaultEvent;
import cq_server.model.BaseChannel;
import cq_server.model.BaseEvent;
import cq_server.model.NetworkMessage;

public class MessageFactory implements IMessageFactory {
	private static final Logger LOG = LoggerFactory.getLogger(MessageFactory.class);

	private final JAXBContext cmdContext;

	private final JAXBContext eventContext;

	private final JAXBContext outMessagesContext;

	public MessageFactory(
			final JAXBContext cmdContext,
			final JAXBContext eventContext,
			final JAXBContext outMessagesContext) {
		this.cmdContext = cmdContext;
		this.eventContext = eventContext;
		this.outMessagesContext = outMessagesContext;
	}

	@Override
	public NetworkMessage createInputNetworkMessage(final String message) {
		final String[] pair = message.split("\r\n");
		LOG.debug("incoming message: channel: {}, event: {}", pair[0], pair[1]);
		final BaseChannel channel = this.getCmdChannel(pair);
		final BaseEvent event = this.getEvent(pair);
		return new NetworkMessage(channel, event);
	}

	@Override
	public String createOutputNetworkMessage(final List<Object> data) {
		try {
			final StringWriter writer = new StringWriter();
			final Marshaller marshaller = this.outMessagesContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			for (final Object object : data)
				marshaller.marshal(object, writer);
			writer.write("\0");
			final String message = writer.toString();
			LOG.debug("outgoing message: {}", message);
			return message;
		} catch (final JAXBException e) {
			LOG.error("{}", data);
			LOG.error("{}", e);
			throw new RuntimeException(e);
		}
	}

	private BaseChannel getCmdChannel(final String[] pair) {
		try {
			return (BaseChannel) this.cmdContext.createUnmarshaller().unmarshal(new StringReader(pair[0]));
		} catch (final JAXBException e) {
			LOG.error("{}", e);
			return new CmdChannel(0, 0, 0);
		}
	}

	private BaseEvent getEvent(final String[] pair) {
		try {
			return (BaseEvent) this.eventContext.createUnmarshaller().unmarshal(new StringReader(pair[1]));
		} catch (final JAXBException e) {
			LOG.error("{}", e);
			return new DefaultEvent();
		}
	}
}
