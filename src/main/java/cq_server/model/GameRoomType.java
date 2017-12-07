package cq_server.model;

public enum GameRoomType {
	SHORT(2), ROOM(5), DUEL(6), MINITOURMNAMENT(10);
	private int type;

	GameRoomType(int type) {
		this.type = type;
	}

	public Integer getType() {
		return type;
	}
}