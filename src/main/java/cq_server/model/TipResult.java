package cq_server.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TIPRESULT")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TipResult {
	private static void sort(final int answer, final List<TipAnswer> results) {
		Collections.sort(results, new Comparator<TipAnswer>() {
			@Override
			public int compare(final TipAnswer o1, final TipAnswer o2) {
				// sorts by 'trueAnswer absolute'
				return (Math.abs(answer - o1.getAnswer()) < Math.abs(answer - o2.getAnswer())) ? -1
						: (Math.abs(answer - o1.getAnswer()) > Math.abs(answer - o2.getAnswer())) ? 1
								: this.doSecodaryOrderSort(o1, o2);
			}

			// If 'trueAnswer absolute' is equal sorts by 'time'
			public int doSecodaryOrderSort(final TipAnswer o1, final TipAnswer o2) {
				return (o1.getTime() < o2.getTime()) ? -1 : (o1.getTime() > o2.getTime()) ? 1 : 0;
			}
		});
	}

	private final int good;

	private int second;

	private int winner;

	public TipResult() {
		this(0);
	}

	public TipResult(final Integer good) {
		this.good = good;
	}

	@XmlAttribute(name = "GOOD")
	public int getGood() {
		return this.good;
	}

	@XmlAttribute(name = "SECOND")
	public Integer getSecond() {
		return this.second;
	}

	@XmlAttribute(name = "WINNER")
	public Integer getWinner() {
		return this.winner;
	}

	public void setResults(final int answer, final List<TipAnswer> results) {
		sort(answer, results);
		this.winner = results.get(0).getId();
		this.second = results.get(1).getId();
	}

	@Override
	public String toString() {
		return "TipResult [good=" + this.good + ", second=" + this.second + ", winner=" + this.winner + "]";
	}
}