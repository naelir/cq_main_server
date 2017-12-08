package cq_server.event;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.CountryMap;
import cq_server.model.EventType;
import cq_server.model.OOPP;
import cq_server.model.Rules;
import cq_server.model.SubRules;

/*
 * <CMD VC="56AD823C2809CF8B5B55EEEA" CONN="4687931" MSGNUM="17" BANN="0" />
 * <ADDSEPROOM MAP="FR" OPP1="" OPP2="" OOPP="2" RULES="2" SEPMESSAGEID="0"
 * QCATS="7" SUBRULES="2" />
 */
@XmlRootElement(name = "ADDSEPROOM")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddSeparateRoomEvent extends BaseEvent {
	@XmlAttribute(name = "MAP")
	@NotNull
	private CountryMap map;

	@XmlAttribute(name = "OPP1")
	@NotNull
	private String opp1;

	@XmlAttribute(name = "OPP2")
	@NotNull
	private String opp2;

	@XmlAttribute(name = "OOPP")
	@NotNull
	private Integer oopp;

	@XmlAttribute(name = "RULES")
	@NotNull
	private Integer rules;

	@XmlAttribute(name = "SEPMESSAGEID")
	@NotNull
	private Integer seppmessageId;

	@XmlAttribute(name = "QCATS")
	@NotNull
	private Integer qcats;

	@XmlAttribute(name = "SUBRULES")
	@NotNull
	private Integer subRules;

	@XmlAttribute(name = "PERSONAL")
	@NotNull
	private Integer personal;

	public AddSeparateRoomEvent() {
		super(EventType.ADDSEPROOM);
	}

	public AddSeparateRoomEvent(final Integer personal, final CountryMap map, final String opp1, final String opp2,
			final Integer oopp, final Integer rules, final Integer subRules, final Integer seppmessageId,
			final Integer qcats) {
		super(EventType.ADDSEPROOM);
		this.personal = personal;
		this.map = map;
		this.opp1 = opp1;
		this.opp2 = opp2;
		this.oopp = oopp;
		this.rules = rules;
		this.subRules = subRules;
		this.seppmessageId = seppmessageId;
		this.qcats = qcats;
	}

	public CountryMap getMap() {
		return this.map;
	}

	public OOPP getOopp() {
		switch (this.oopp) {
		case 1:
			return OOPP.ANYONE;
		case 2:
			return OOPP.HASROBOT;
		case 3:
			return OOPP.WITH_INVITED_PLAYERS;
		default:
			return OOPP.ANYONE;
		}
	}

	public String getOpp1() {
		return this.opp1;
	}

	public String getOpp2() {
		return this.opp2;
	}

	public Integer getPersonal() {
		return this.personal;
	}

	public Integer getQcats() {
		return this.qcats;
	}

	public Rules getRules() {
		switch (this.rules) {
		case 1:
			return Rules.LONG;
		case 2:
			return Rules.SHORT;
		case 3:
			return Rules.DUEL;
		default:
			return Rules.SHORT;
		}
	}

	public Integer getSeppmessageId() {
		return this.seppmessageId;
	}

	public SubRules getSubRules() {
		switch (this.rules) {
		case 0:
			return SubRules.NORMAL;
		case 1:
			return SubRules.NOBASEATTACK;
		case 2:
			return SubRules.LASTMANSTANDING;
		default:
			return SubRules.NORMAL;
		}
	}

	public void setMap(final CountryMap map) {
		this.map = map;
	}

	public void setOopp(final Integer oopp) {
		this.oopp = oopp;
	}

	public void setOpp1(final String opp1) {
		this.opp1 = opp1;
	}

	public void setOpp2(final String opp2) {
		this.opp2 = opp2;
	}

	public void setPersonal(final Integer personal) {
		this.personal = personal;
	}

	public void setQcats(final Integer qcats) {
		this.qcats = qcats;
	}

	public void setRules(final Integer rules) {
		this.rules = rules;
	}

	public void setSeppmessageId(final Integer seppmessageId) {
		this.seppmessageId = seppmessageId;
	}

	public void setSubRules(final Integer subRules) {
		this.subRules = subRules;
	}
}
