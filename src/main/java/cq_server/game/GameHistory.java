package cq_server.game;

import java.util.Deque;

import cq_server.model.QuestionContext;
import cq_server.model.TipQuestionContext;
import cq_server.model.WinnerSelect;

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

	public boolean isPlayerSendTip(final Integer playerId) {
		return this.tiphistory.peekLast().getTipinfo().getTimeOrder().contains(playerId);
	}

	public void offerQuestion(final QuestionContext questionContext) {
		this.qhistory.offer(questionContext);
	}

	public void offerTip(final TipQuestionContext tipQuestionContext) {
		this.tiphistory.offer(tipQuestionContext);
	}

	public void offerWinner(final WinnerSelect winnerSelect) {
		this.spreadingHistory.offer(winnerSelect);
	}

	public QuestionContext peekLastQuestion() {
		return this.qhistory.peekLast();
	}

	public TipQuestionContext peekLastTip() {
		return this.tiphistory.peekLast();
	}

	public WinnerSelect peekLastWinner() {
		return this.spreadingHistory.peekLast();
	}
}
