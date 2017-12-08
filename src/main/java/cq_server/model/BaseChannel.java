package cq_server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CMDBASE")
@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class BaseChannel {
	private Integer connid;

	private Integer msgnum;

	private Integer result;

	public BaseChannel() {
		// TODO Auto-generated constructor stub
	}

	public BaseChannel(final Integer connid, final Integer msgnum, final Integer result) {
		this.connid = connid;
		this.msgnum = msgnum;
		this.result = result;
	}

	@XmlAttribute(name = "CONN")
	public Integer getConnid() {
		return this.connid;
	}

	@XmlAttribute(name = "MSGNUM")
	public Integer getMsgnum() {
		return this.msgnum;
	}

	@XmlAttribute(name = "RESULT")
	public Integer getResult() {
		return this.result;
	}

	public void setConnid(final Integer connid) {
		this.connid = connid;
	}

	public void setConnId(final int id) {
		this.connid = id;
	}

	public void setMsgnum(final Integer msgnum) {
		this.msgnum = msgnum;
	}

	public void setResult(final Integer result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "BaseChannel [connid=" + this.connid + ", msgnum=" + this.msgnum + ", result=" + this.result
				+ ", getClass()=" + this.getClass() + "]";
	}

	public enum Type {
		LISTEN, CMD;
	}
}
