package cq_server.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

@XmlRootElement(name = "TIPINFO")
public class TipInfo {
	private int trueAnswer;

	private int players;

	private List<Integer> timeOrder;

	private List<TipAnswer> tipAnswers;

	private long startTime;

	@XmlAttribute(name = "TIMEORDER")
	private String torder;

	@XmlAttribute(name = "T1")
	private String t1;

	@XmlAttribute(name = "T2")
	private String t2;

	@XmlAttribute(name = "T3")
	private String t3;

	@XmlAttribute(name = "V1")
	private Integer v1;

	@XmlAttribute(name = "V2")
	private Integer v2;

	@XmlAttribute(name = "V3")
	private Integer v3;

	public TipInfo() {
		// TODO Auto-generated constructor stub
	}

	public TipInfo(final int good, final int players) {
		super();
		this.trueAnswer = good;
		this.players = players;
		this.timeOrder = new ArrayList<>(players);
		this.tipAnswers = new ArrayList<>(players);
		this.startTime = System.currentTimeMillis();
	}

	public void calculate() {
		for (final TipAnswer t : this.tipAnswers)
			switch (t.getId()) {
			case 1:
				this.v1 = t.getAnswer();
				break;
			case 2:
				this.v2 = t.getAnswer();
				break;
			case 3:
				this.v3 = t.getAnswer();
				break;
			default:
				break;
			}
	}

	public int getAnswer() {
		return this.trueAnswer;
	}

	public List<Integer> getTimeOrder() {
		return this.timeOrder;
	}

	public List<TipAnswer> getTipAnswers() {
		return this.tipAnswers;
	}

	public boolean isReady() {
		return this.tipAnswers.size() == this.players;
	}

	public void setTip(final Integer playerId, final Integer tipAnswer) {
		final long time = (System.currentTimeMillis() - this.startTime);
		this.tipAnswers.add(new TipAnswer(playerId, tipAnswer, time));
		this.timeOrder.add(playerId);
		switch (playerId) {
		case 1:
			this.t1 = String.format("%.3f", time / 1000.0);
			break;
		case 2:
			this.t2 = String.format("%.3f", time / 1000.0);
			break;
		case 3:
			this.t3 = String.format("%.3f", time / 1000.0);
			break;
		default:
			break;
		}
		this.torder = StringUtils.join(this.timeOrder, "");
	}

	@Override
	public String toString() {
		return "TipInfo [trueAnswer=" + this.trueAnswer + ", players=" + this.players + ", timeOrder=" + this.timeOrder
				+ ", tipAnswers=" + this.tipAnswers + ", startTime=" + this.startTime + ", torder=" + this.torder
				+ ", t1=" + this.t1 + ", t2=" + this.t2 + ", t3=" + this.t3 + ", v1=" + this.v1 + ", v2=" + this.v2
				+ ", v3=" + this.v3 + "]";
	}
}