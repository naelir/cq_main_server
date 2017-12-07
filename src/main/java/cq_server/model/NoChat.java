package cq_server.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NOCHAT")
public final class NoChat {
	private static String getParam(final Calendar toDate) {
		return new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(toDate.getTime());
	}

	@XmlAttribute(name = "MODE")
	private  int mode;

	@XmlAttribute(name = "PARAM")
	private  String param;

	@XmlAttribute(name = "REASON")
	private  String reason;

	public NoChat() {
		this(0, null, null);
	}

	public NoChat(final Calendar toDate) {
		this(1, getParam(toDate), "");
	}
 
	public NoChat(final int mode, final String param, final String reason) {
		super();
		this.mode = mode;
		this.param = param;
		this.reason = reason;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}