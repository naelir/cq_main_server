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
	private int good;

	private int second;

	private int winner;

	public TipResult() {
		this(0);
	}

	public TipResult(Integer good) {
		this.good = good;
	}

	@XmlAttribute(name = "GOOD")
	public int getGood() {
		return good;
	}

	@XmlAttribute(name = "SECOND")
	public Integer getSecond() {
		return second;
	}

	@XmlAttribute(name = "WINNER")
	public Integer getWinner() {
		return winner;
	}

	public void setResults(int answer, List<TipAnswer> results) {
		sort(answer, results);
		winner = results.get(0).getId();
		second = results.get(1).getId();
	}

	private void sort(int answer, List<TipAnswer> results) {
		Collections.sort(results, new Comparator<TipAnswer>() {
			@Override
			public int compare(TipAnswer o1, TipAnswer o2) {
				// sorts by 'trueAnswer absolute'
				return (Math.abs(answer - o1.getAnswer()) < Math.abs(answer - o2.getAnswer())) ? -1
						: (Math.abs(answer - o1.getAnswer()) > Math.abs(answer - o2.getAnswer())) ? 1
								: doSecodaryOrderSort(o1, o2);
			}

			// If 'trueAnswer absolute' is equal sorts by 'time'
			public int doSecodaryOrderSort(TipAnswer o1, TipAnswer o2) {
				return (o1.getTime() < o2.getTime()) ? -1 : (o1.getTime() > o2.getTime()) ? 1 : 0;
			}
		});
	}

	@Override
	public String toString() {
		return "TipResult [good=" + good + ", second=" + second + ", winner=" + winner + "]";
	}
}