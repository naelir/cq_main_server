package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseChannel;

@XmlRootElement(name = "LISTEN")
@XmlAccessorType(XmlAccessType.FIELD)
public final class ListenChannel extends BaseChannel {
	public ListenChannel() {
		super(0, 0, 0);
	}

	public ListenChannel(final Integer connid, final Integer msgnum, Integer result) {
		super(connid, msgnum, result);
	}
}