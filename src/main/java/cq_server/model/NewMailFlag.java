package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NEWMAILFLAG")
public final class NewMailFlag {
	@XmlAttribute(name = "FLAG")
	private final int flag;

	public NewMailFlag() {
		this.flag = 0;
	}
}