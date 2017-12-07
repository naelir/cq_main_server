package cq_server.model;

import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "WAITSTATE")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class WaitState {
	private AtomicInteger roomSel;

	private AtomicInteger rsdms;

	private AtomicInteger sepRoomSel;

	public WaitState() {
	}

	public WaitState(int sepRoomSel, int roomSel) {
		this.sepRoomSel = new AtomicInteger(sepRoomSel);
		this.roomSel = new AtomicInteger(roomSel);
		this.rsdms = new AtomicInteger(0); 
	}

	@XmlAttribute(name = "ROOMSEL")
	public int getRoomSel() {
		return this.roomSel.get();
	}

	@XmlAttribute(name = "RSDM")
	public int getRsdms() {
		return this.rsdms.get();
	}

	@XmlAttribute(name = "SEPROOMSEL")
	public int getSepRoomSel() {
		return this.sepRoomSel.get();
	}

	public void setRoomSel(int roomSel) {
		this.roomSel.set(roomSel);
	}

	public void setSepRoomSel(int sepRoomSel) {
		this.sepRoomSel.set(sepRoomSel);
	}
}