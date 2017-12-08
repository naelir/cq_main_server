package cq_server.model;

import javax.xml.bind.annotation.*;

@SuppressWarnings("unused")
//<MYDATA NAME=\"%s\" ID=\"%d\" RIGHTS=\"%d\" JEP=\"%d\" JEPGAMES=\"%d\" VEP=\"%d\" 
//AOJ=\"%d\" GWR=\"%d\" GAMECOUNT=\"%d\" DAYRANK=\"%d\" CLP=\"%d\" GOLDS=\"%d\" 
//SILVERS=\"%d\" PREMIUMSERVICES=\"{}\" SEX=\"%d\" BIRTHYEAR=\"%d\" AGE=\"\" LOCATIONID=\"%d\" 
//FACE=\"%d\" CHATCOLOR=\"%d\" SINFO=\"\" TOURRES=\"0,0,0,0\" MTOURRES=\"0,0,0\" />
@XmlRootElement(name = "MYDATA")
@XmlAccessorType(XmlAccessType.FIELD)
public final class MyData {
	@XmlAttribute(name = "AGE")
	private int age;

	@XmlAttribute(name = "AOJ")
	private int aoj;

	@XmlAttribute(name = "BIRTHYEAR")
	private int birthday;

	@XmlAttribute(name = "CHATCOLOR")
	private int chatcolor;

	@XmlAttribute(name = "CLP")
	private int clp;

	@XmlAttribute(name = "DAYRANK")
	private int dayrank;

	@XmlAttribute(name = "FACE")
	private int face;

	@XmlAttribute(name = "GAMECOUNT")
	private final int gamecount = 30;

	@XmlAttribute(name = "GOLDS")
	private int golds;

	@XmlAttribute(name = "GWR")
	private int gwr;

	@XmlAttribute(name = "ID")
	int id;

	@XmlAttribute(name = "JEP")
	private final int jep = 10000;

	@XmlAttribute(name = "JEPGAMES")
	private final int jepgames = 30;

	@XmlAttribute(name = "LOCATIONID")
	private int locationid;

	@XmlAttribute(name = "NAME")
	private String name;

	@XmlAttribute(name = "SINFO")
	private final String sinfo = "";

	@XmlAttribute(name = "TOURRES")
	private final String tourres = "0,0,0,0";

	@XmlAttribute(name = "MTOURRES")
	private final String mtourres = "0,0,0";

	private String pass;

	@XmlAttribute(name = "RIGHTS")
	private int rights;

	// private String premiumservices = "{}"
	@XmlTransient
	private int sex;

	@XmlAttribute(name = "SILVERS")
	private int silvers;

	@XmlAttribute(name = "USERS")
	private final boolean simple = false;

	@XmlAttribute(name = "VEP")
	private int vep;

	public MyData() {
		this(0, "0");
	}

	public MyData(final int id, final String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}