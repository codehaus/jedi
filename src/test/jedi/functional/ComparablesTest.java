package jedi.functional;

import java.util.Comparator;
import java.util.HashSet;

public class ComparablesTest extends ClosureTestCase {

    public void testEqual() {
    	Filter<String> equal = Comparables.equal("b", String.CASE_INSENSITIVE_ORDER);
    	assertFalse(equal.execute("a"));
    	assertTrue(equal.execute("b"));
    	assertFalse(equal.execute("c"));
    }

    public void testGreaterThan() {
    	Filter<String> greaterThan = Comparables.greaterThan("b", String.CASE_INSENSITIVE_ORDER);
    	assertFalse(greaterThan.execute("a"));
    	assertFalse(greaterThan.execute("b"));
    	assertTrue(greaterThan.execute("c"));
    }

    public void testLessThan() {
    	Filter<String> lessThan = Comparables.lessThan("b", String.CASE_INSENSITIVE_ORDER);
    	assertTrue(lessThan.execute("a"));
    	assertFalse(lessThan.execute("b"));
    	assertFalse(lessThan.execute("c"));
    }

    public void testMaxReturnsTheDefaultValueIfTheCollectionIsEmpty() {
        assertEquals("a", Comparables.max("a", new HashSet<String>()));
    }

    public void testMaxReturnsTheMaximumValueOfACollection() {
        assertEquals("c", Comparables.max(Coercions.set("a", "b", "c")));
    }

    @SuppressWarnings("unchecked")
    public void testMaxThrowsAnAssertionErrorForEmptyCollections() {
        try {
            Comparables.max(new HashSet<Comparable>());
        } catch (jedi.assertion.AssertionError e) {
            // Expected
        }
    }

    public void testMinReturnsTheDefaultValueIfTheCollectionIsEmpty() {
        assertEquals("a", Comparables.min("a", new HashSet<String>()));
    }

    public void testMinReturnsTheMinimumValueOfACollection() {
        assertEquals("a", Comparables.min(Coercions.set("a", "b", "c")));
    }

    @SuppressWarnings("unchecked")
    public void testMinThrowsAnAssertionErrorForEmptyCollections() {
        try {
            Comparables.min(new HashSet<Comparable>());
        } catch (jedi.assertion.AssertionError e) {
            // Expected
        }
    }

    public void testSortWithComparable() {
    	assertEquals(Coercions.list(1, 2), Comparables.sortInPlace(Coercions.list(2, 1)));
    }

    public void testSortWithComparator() {
    	assertEquals(Coercions.list(3, 2, 1), Comparables.sortInPlace(Coercions.list(1, 3, 2), new Comparator<Integer>() {
    		public int compare(Integer o1, Integer o2) {
    			return o2.compareTo(o1);
    		}
    	}));
    }

    public void testSortWithEvaluator() {
        assertEquals(Coercions.list("1", "2", "3"), Comparables.sortInPlace(Coercions.list("3", "1", "2"), new Functor<String, Integer>() {
            public Integer execute(String value) {
                return Integer.parseInt(value);
            }
        }));
    }

}
