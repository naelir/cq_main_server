package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NEWREG")
public final class NewReg {
	@XmlAttribute(name = "REGLEVEL")
	private final int regLevel;

	public NewReg() {
		regLevel = 0;
	}
}