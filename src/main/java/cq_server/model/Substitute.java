package cq_server.model;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SEPROOM")
public final class Substitute {
	@XmlAttribute(name = "PLAYER")
	private final int player;

	@XmlAttribute(name = "NAME")
	private final String name;

	@XmlAttribute(name = "HC")
	private final List<Integer> hc;

	public Substitute() {
		this(0, null, Arrays.asList(0));
	}

	public Substitute(int player, String name, List<Integer> hc) {
		super();
		this.player = player;
		this.name = name;
		this.hc = hc;
	}
}