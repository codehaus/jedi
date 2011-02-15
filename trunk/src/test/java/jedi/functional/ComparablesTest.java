package jedi.functional;

import static jedi.functional.Coercions.list;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

public class ComparablesTest extends ClosureTestCase {

	@Test
	public void testEqual() {
		Filter<String> equal = Comparables.equal("b", String.CASE_INSENSITIVE_ORDER);
		assertFalse(equal.execute("a"));
		assertTrue(equal.execute("b"));
		assertFalse(equal.execute("c"));
	}

	@Test
	public void testGreaterThan() {
		Filter<String> greaterThan = Comparables.greaterThan("b", String.CASE_INSENSITIVE_ORDER);
		assertFalse(greaterThan.execute("a"));
		assertFalse(greaterThan.execute("b"));
		assertTrue(greaterThan.execute("c"));
	}

	@Test
	public void testLessThan() {
		Filter<String> lessThan = Comparables.lessThan("b", String.CASE_INSENSITIVE_ORDER);
		assertTrue(lessThan.execute("a"));
		assertFalse(lessThan.execute("b"));
		assertFalse(lessThan.execute("c"));
	}

	@Test
	public void testMaxReturnsTheDefaultValueIfTheCollectionIsEmpty() {
		assertEquals("a", Comparables.max("a", new HashSet<String>()));
	}

	@Test
	public void testMaxReturnsTheMaximumValueOfACollection() {
		assertEquals("c", Comparables.max(Coercions.set("a", "b", "c")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMaxThrowsAnAssertionErrorForEmptyCollections() {
		try {
			Comparables.max(new HashSet<Comparable>());
		} catch (jedi.assertion.AssertionError e) {
			// Expected
		}
	}

	@Test
	public void testMinReturnsTheDefaultValueIfTheCollectionIsEmpty() {
		assertEquals("a", Comparables.min("a", new HashSet<String>()));
	}

	@Test
	public void testMinReturnsTheMinimumValueOfACollection() {
		assertEquals("a", Comparables.min(Coercions.set("a", "b", "c")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMinThrowsAnAssertionErrorForEmptyCollections() {
		try {
			Comparables.min(new HashSet<Comparable>());
		} catch (jedi.assertion.AssertionError e) {
			// Expected
		}
	}

	@Test
	public void testSort() throws Exception {
		List<String> toSort = list("b", "b", "a", "z");
		List<String> sorted = Comparables.sort(toSort);
		assertEquals(list("a", "b", "b", "z"), sorted);
		assertFalse(toSort == sorted);
	}

	@Test
	public void testSortWithComparator() throws Exception {
		List<String> toSort = list("b", "b", "a", "z");
		List<String> sorted = Comparables.sort(toSort, String.CASE_INSENSITIVE_ORDER);
		assertEquals(list("a", "b", "b", "z"), sorted);
		assertFalse(toSort == sorted);
	}

	@Test
	public void testSortWithEvaluator() {
		List<String> list = list("3", "1", "2");
		List<String> result = Comparables.sort(list, new Functor<String, Integer>() {
			public Integer execute(String value) {
				return Integer.parseInt(value);
			}
		});
		assertEquals(list("1", "2", "3"), result);
		assertFalse(list == result);
	}

	@Test
	public void testSortInPlaceWithComparable() {
		List<Integer> list = list(2, 1);
		List<Integer> result = Comparables.sortInPlace(list);
		assertEquals(list(1, 2), result);
		assertSame(list, result);
	}

	@Test
	public void testSortInPlaceWithComparator() {
		List<Integer> list = list(1, 3, 2);
		List<Integer> result = Comparables.sortInPlace(list, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		});
		assertEquals(list(3, 2, 1), result);
		assertSame(list, result);
	}

	@Test
	public void testSortInPlaceWithEvaluator() {
		List<String> list = list("3", "1", "2");
		List<String> result = Comparables.sortInPlace(list, new Functor<String, Integer>() {
			public Integer execute(String value) {
				return Integer.parseInt(value);
			}
		});
		assertEquals(list("1", "2", "3"), result);
		assertSame(list, result);
	}

}
