package cq_server.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommonTests {
	private static final List<Integer> DEFAULT_WARORDER = Arrays.asList(1, 2, 3, 2, 3, 1, 3, 1, 2, 4, 4, 4);

	public static void main(final String[] args) {
		System.out.println(DEFAULT_WARORDER.subList(0, 9));
		final List<Integer> subList = DEFAULT_WARORDER.subList(0, 9);
		final List<Integer> warOrder = new ArrayList<>();
		warOrder.addAll(subList);
		warOrder.addAll(Arrays.asList(2, 1, 3));
		System.out.println(warOrder);
		final List<Integer> list = new CopyOnWriteArrayList<>(Arrays.asList(2, 3, 2));
		System.out.println(list);
		list.remove(2);
		System.out.println(list);
	}
}
