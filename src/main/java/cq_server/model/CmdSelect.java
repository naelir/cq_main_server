package cq_server.model;

import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CMD")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class CmdSelect {
	private final CmdType type;

	private final Set<Integer> availableAreas;

	private final int mapHelps;

	public CmdSelect() {
		this(Collections.emptySet(), 0);
	}

	public CmdSelect(final Set<Integer> availableAreas, final Integer mapHelps) {
		this.type = CmdType.SELECT;
		this.availableAreas = availableAreas;
		this.mapHelps = mapHelps;
	}

	@XmlAttribute(name = "AVAILABLE")
	public String getAvailable() {
		int available = 0;
		for (final Integer area : this.availableAreas)
			available = available | 1 << (area.intValue() - 1);
		return Integer.toHexString(available);
	}

	public Set<Integer> getAvailableAreas() {
		return this.availableAreas;
	}

	@XmlAttribute(name = "MAPHELPS")
	public int getMapHelps() {
		return this.mapHelps;
	}

	@XmlAttribute(name = "CMD")
	public CmdType getType() {
		return this.type;
	}
}
