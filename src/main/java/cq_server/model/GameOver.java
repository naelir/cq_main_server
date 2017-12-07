package cq_server.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GAMEOVER")
/*
 * <GAMEOVER PLACINGS=
 * \"%s\" NAME1=\"%s\" NAME2=\"%s\" NAME3=\"%s\" DIVISION=\"%d\" IAM=\"%d\" GWR=\"%d\" "
 * "AOJ=\"%d\" VEP=\"%d\" GOODANSCOUNT=\"%d\" ANSCNT=\"%d\" TIPCNT=\"%d\" NEWJEP=\"%d\" "
 * "JEPCHANGE=\"%d\" MONEY=\"%d\" MONEYCHANGE=\"%d\" VEPTIPCNT=\"%s\" SELVEP=\"%s\" "
 * + "TIPVEP=\"%s\" SELVEPCH=\"%s\" TIPVEPCH=\"%s\"/>
 */
public class GameOver {
	@XmlAttribute(name = "PLACINGS")
	private String placings;

	@XmlAttribute(name = "NAME1")
	private String name1;

	@XmlAttribute(name = "NAME2")
	private String name2;

	@XmlAttribute(name = "NAME3")
	private String name3;

	@XmlAttribute(name = "DIVISION")
	private int division;

	@XmlAttribute(name = "IAM")
	private int iam;

	@XmlAttribute(name = "GWR")
	private int gwr;

	@XmlAttribute(name = "AOJ")
	private int aoj;

	@XmlAttribute(name = "VEP")
	private int vep;

	@XmlAttribute(name = "GOODANSCOUNT")
	private int goodAnsCount;

	@XmlAttribute(name = "ANSCNT")
	private int ansCnt;

	@XmlAttribute(name = "TIPCNT")
	private int tipCnt;

	@XmlAttribute(name = "NEWJEP")
	private int newJep;

	@XmlAttribute(name = "JEPCHANGE")
	private int jepChange;

	@XmlAttribute(name = "MONEY")
	private int money;

	@XmlAttribute(name = "MONEYCHANGE")
	private int moneyChange;

	@XmlAttribute(name = "VEPTIPCNT")
	private String vepTipCnt;

	@XmlAttribute(name = "SELVEP")
	private String selVep;

	@XmlAttribute(name = "TIPVEP")
	private String tipVep;

	@XmlAttribute(name = "SELVEPCH")
	private String selVepCh;

	@XmlAttribute(name = "TIPVEPCH")
	private String tipVepCh;

	public GameOver() {
		// TODO Auto-generated constructor stub
	}

	public GameOver(final Builder builder) {
		super();
		this.placings = builder.placings;
		this.name1 = builder.name1;
		this.name2 = builder.name2;
		this.name3 = builder.name3;
		this.division = builder.division;
		this.iam = builder.iam;
		this.gwr = builder.gwr;
		this.aoj = builder.aoj;
		this.vep = builder.vep;
		this.goodAnsCount = builder.goodAnsCount;
		this.ansCnt = builder.ansCnt;
		this.tipCnt = builder.tipCnt;
		this.newJep = builder.newJep;
		this.jepChange = builder.jepChange;
		this.money = builder.money;
		this.moneyChange = builder.moneyChange;
		this.vepTipCnt = String.format("%.2f", builder.vepTipCnt);
		this.selVep = String.format("%.2f", builder.selVep);
		this.tipVep = String.format("%.2f", builder.tipVep);
		this.selVepCh = String.format("%.2f", builder.selVepCh);
		this.tipVepCh = String.format("%.2f", builder.tipVepCh);
	}

	public static final class Builder {
		private String placings, name1, name2, name3;

		private int division, iam, gwr, aoj, vep, goodAnsCount, ansCnt, tipCnt, newJep, jepChange, money, moneyChange;

		private double vepTipCnt, selVep, tipVep, selVepCh, tipVepCh;

		public Builder() {
			super();
		}

		public GameOver build() {
			return new GameOver(this);
		}

		public Builder setAnsCnt(final int ansCnt) {
			this.ansCnt = ansCnt;
			return this;
		}

		public Builder setAoj(final int aoj) {
			this.aoj = aoj;
			return this;
		}

		public Builder setDivision(final int division) {
			this.division = division;
			return this;
		}

		public Builder setGoodAnsCount(final int goodAnsCount) {
			this.goodAnsCount = goodAnsCount;
			return this;
		}

		public Builder setGwr(final int gwr) {
			this.gwr = gwr;
			return this;
		}

		public Builder setIam(final int iam) {
			this.iam = iam;
			return this;
		}

		public Builder setJepChange(final int jepChange) {
			this.jepChange = jepChange;
			return this;
		}

		public Builder setMoney(final int money) {
			this.money = money;
			return this;
		}

		public Builder setMoneyChange(final int moneyChange) {
			this.moneyChange = moneyChange;
			return this;
		}

		public Builder setName1(final String name1) {
			this.name1 = name1;
			return this;
		}

		public Builder setName2(final String name2) {
			this.name2 = name2;
			return this;
		}

		public Builder setName3(final String name3) {
			this.name3 = name3;
			return this;
		}

		public Builder setNewJep(final int newJep) {
			this.newJep = newJep;
			return this;
		}

		public Builder setPlacings(final String placings) {
			this.placings = placings;
			return this;
		}

		public Builder setSelVep(final double selVep) {
			this.selVep = selVep;
			return this;
		}

		public Builder setSelVepCh(final double selVepCh) {
			this.selVepCh = selVepCh;
			return this;
		}

		public Builder setTipCnt(final int tipCnt) {
			this.tipCnt = tipCnt;
			return this;
		}

		public Builder setTipVep(final double tipVep) {
			this.tipVep = tipVep;
			return this;
		}

		public Builder setTipVepCh(final double tipVepCh) {
			this.tipVepCh = tipVepCh;
			return this;
		}

		public Builder setVep(final int vep) {
			this.vep = vep;
			return this;
		}

		public Builder setVepTipCnt(final double vepTipCnt) {
			this.vepTipCnt = vepTipCnt;
			return this;
		}
	}
}
