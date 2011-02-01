package jedi.example;

import static jedi.functional.Box.boxInts;
import static jedi.functional.Coercions.asList;
import static jedi.functional.Coercions.list;

import java.util.Collection;
import java.util.Comparator;

import junit.framework.TestCase;

public class QuickSortTest extends TestCase {

	public void testSorts() throws Exception {
		Collection<Integer> result = QuickSort.sort(asList(boxInts(1, 4, 3, 1, 8, 0)), new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		assertEquals(asList(boxInts(0, 1, 1, 3, 4, 8)), result);
	}

	public void testSort2() throws Exception {
		Collection<String> result = QuickSort.sort(list("x", "d", "a"), String.CASE_INSENSITIVE_ORDER);
		assertEquals(list("a", "d", "x"), result);
	}

	public void testSortEmptyList() throws Exception {
		Collection<String> result = QuickSort.sort(asList(new String[] {}), String.CASE_INSENSITIVE_ORDER);
		assertEquals(asList(new String[] {}), result);
	}
}
