package cq_server.model;

import java.util.Deque;

public class GameHistory {
	private final Deque<WinnerSelect> spreadingHistory;

	private final Deque<QuestionContext> qhistory;

	private final Deque<TipQuestionContext> tiphistory;

	public GameHistory(
			final Deque<WinnerSelect> spreadingHistory,
			final Deque<QuestionContext> qhistory,
			final Deque<TipQuestionContext> tiphistory) {
		this.spreadingHistory = spreadingHistory;
		this.qhistory = qhistory;
		this.tiphistory = tiphistory;
	}

	public Deque<QuestionContext> getQhistory() {
		return this.qhistory;
	}

	public Deque<WinnerSelect> getSpreadingHistory() {
		return this.spreadingHistory;
	}

	public Deque<TipQuestionContext> getTiphistory() {
		return this.tiphistory;
	}
}
