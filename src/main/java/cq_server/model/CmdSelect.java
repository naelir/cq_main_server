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

	public CmdSelect(Set<Integer> availableAreas, Integer mapHelps) {
		this.type = CmdType.SELECT;
		this.availableAreas = availableAreas;
		this.mapHelps = mapHelps;
	}

	public Set<Integer> getAvailableAreas() {
		return availableAreas;
	}

	@XmlAttribute(name = "AVAILABLE")
	public String getAvailable() {
		int available = 0;
		for (Integer area : availableAreas)
			available = available | 1 << (area.intValue() - 1);
		return Integer.toHexString(available);
	}

	@XmlAttribute(name = "MAPHELPS")
	public int getMapHelps() {
		return mapHelps;
	}

	@XmlAttribute(name = "CMD")
	public CmdType getType() {
		return type;
	}
}
