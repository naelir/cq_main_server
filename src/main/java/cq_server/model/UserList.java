package cq_server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import cq_server.game.BasePlayer;

/*
 * <USERLIST STATEID="3553727" FULLUL="graou206|odisaure57|warren s |iamPepi|Big
 * Soul|grouchat^1|Stonewall^2|jack barron|e1000littre
 * |rangboom|Roquepine|Cosette^2|BOBANDBLUE|Ti tus|dickinson
 * |SayaClau|Keke3350|endy|Ninsun|dav63|Charlie 1er|doctoryann"
 * ULCH="-Keke3350|-BOBANDBLUE|+Foxy Brown|+AC00|+Keke3350|-AC00
 * |+AC00|+Chapo|-Chapo|-AC00|-Keke3350|-dav63|+will0thewisp" />
 * 
 */
@XmlRootElement(name = "USERLIST")
public class UserList {
	private final List<BasePlayer> users;

	@XmlAttribute(name = "STATEID")
	private Integer lastId;

	@XmlAttribute(name = "FULLUL")
	private String fullUl;

	@XmlAttribute(name = "ULCH")
	private final String ulch;

	private final List<Change> changes;

	public UserList() {
		this.users = new CopyOnWriteArrayList<>();
		this.lastId = 0;
		this.changes = Collections.synchronizedList(new ArrayList<>());
		this.ulch = "";
	}

	public void add(final BasePlayer player) {
		this.users.add(player);
		this.changes.add(new Change(this.lastId++, player.getName(), Operation.ADD));
		this.fullUl = StringUtils.join(this.users.stream().map(a -> a.getName()).collect(Collectors.toList()), '|');
	}

	public void remove(final BasePlayer player) {
		this.users.remove(player);
		this.changes.add(new Change(this.lastId++, player.getName(), Operation.REMOVE));
		this.fullUl = StringUtils.join(this.users.stream().map(a -> a.getName()).collect(Collectors.toList()), '|');
	}

	public Optional<BasePlayer> get(final String name) {
		return this.users.stream().filter(element -> element.getName().equals(name)).findAny();
	}

	public int getUlState() {
		return this.lastId;
	}

	public enum Operation {
		ADD("+"), REMOVE("-");
		private String sign;

		private Operation(final String sign) {
			this.sign = sign;
		}

		public String getSign() {
			return this.sign;
		}
	}

	public static class Change {
		int id;

		String name;

		Operation operation;

		public Change(final int id, final String name, final Operation operation) {
			super();
			this.id = id;
			this.name = name;
			this.operation = operation;
		}

		public int getId() {
			return this.id;
		}

		public Operation getState() {
			return this.operation;
		}

		public String getUser() {
			return this.name;
		}
	}
}
