package cq_server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CMD")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class CmdAnswer {
	private final CmdType type;

	public CmdAnswer() {
		this.type = CmdType.ANSWER;
	}

	@XmlAttribute(name = "CMD")
	public CmdType getType() {
		return this.type;
	}
}
