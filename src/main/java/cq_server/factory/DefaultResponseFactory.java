package cq_server.factory;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultResponseFactory implements IResponseFactory {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultResponseFactory.class);

	private final JAXBContext responseContext;

	public DefaultResponseFactory(final JAXBContext responseContext) {
		this.responseContext = responseContext;
	}

	@Override
	public String getResponse(final List<?> list) {
		try {
			final StringWriter writer = new StringWriter();
			final Marshaller marshaller = this.responseContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			for (final Object object : list)
				marshaller.marshal(object, writer);
			writer.write("\0");
			final String message = writer.toString();
			LOG.debug("outgoing message: {}", message);
			return message;
		} catch (final JAXBException e) {
			LOG.error("{}:", list, e);
			throw new RuntimeException(e);
		}
	}
}
