package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseChannel;

@XmlRootElement(name = "CMD")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmdChannel extends BaseChannel {
	public CmdChannel() {
		super(0, 0, 0);
	}

	public CmdChannel(Integer connid, Integer msgnum, Integer result) {
		super(connid, msgnum, result);
	}
	
}