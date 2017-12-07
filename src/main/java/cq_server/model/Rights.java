package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "STATE")
public final class Rights {
	@XmlAttribute(name = "SCREEN")
	private final String screen;

	@XmlAttribute(name = "FLAGS")
	private final int flags;

	@XmlAttribute(name = "RIGHTS")
	private final int rights;

	public Rights() {
		this.flags = 0;
		this.rights = 0;
		this.screen = "WAIT";
	}
}