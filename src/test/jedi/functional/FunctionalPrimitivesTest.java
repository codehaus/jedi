package jedi.functional;

import static jedi.functional.Coercions.*;
import static jedi.functional.FunctionalPrimitives.*;
import org.jmock.Mock;
import org.jmock.util.Dummy;

import java.util.*;

public class FunctionalPrimitivesTest extends ClosureTestCase {
    @SuppressWarnings("unchecked")
    public void testListWithDifferentTypes() {
        List<? extends Number> list = Coercions.list(INTEGER_ONE, DOUBLE_TWO);
        assertSame(list.get(0), INTEGER_ONE);
        assertSame(list.get(1), DOUBLE_TWO);
    }

	@SuppressWarnings("unchecked")
	public void testOnlyReturnsTheHeadOfASingleElementCollection() {
		List<Object> list = Coercions.list(new Object());
		assertSame(head(list), only(list));
	}

	public void testOnlyThrowsAnAssertionErrorIfTheCollectionContainsNoElements() {
		try {
			only(Coercions.list());
			fail();
		} catch (jedi.assertion.AssertionError e) {
			// As expected
		}
	}

	public void testOnlyThrowsAnAssertionErrorIfTheCollectionContainsMoreThanOneElement() {
		try {
			only(Coercions.list(1, 2));
			fail();
		} catch (jedi.assertion.AssertionError e) {
			// As expected
		}
	}

	@SuppressWarnings("unchecked")
	public void testCollectReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
		List list = collect(Coercions.list(), functor);
		assertTrue(list.isEmpty());
	}

	@SuppressWarnings("unchecked")
	public void testCollectReturnsAListOfItemsMappedByTheGivenFunctor() {
		List<Object> in = Coercions.list(new Object(), new Object());
		List<Object> expectedOut = Coercions.list(new Object(), new Object());

		assertEquals(expectedOut, collect(in, setUpFunctorExpectations(in, expectedOut)));
	}

	@SuppressWarnings("unchecked")
	public void testFlattenReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
		assertTrue(flatten(Coercions.list(), functor).isEmpty());
	}

	@SuppressWarnings("unchecked")
	public void testFlattenReturnsAggregatedList() {
		List inputValues = Coercions.list(1, 2);
		setUpFunctorExpectations(inputValues, Coercions.list(Coercions.list("A", "B"), Coercions.list("C", "D")));
		assertEquals(Coercions.list("A", "B", "C", "D"), flatten(inputValues, functor));
	}

	@SuppressWarnings("unchecked")
	public void testSelectReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
		List selected = select(Coercions.list(), (Filter) mock(Filter.class).proxy());
		assertTrue(selected.isEmpty());
	}

    @SuppressWarnings("unchecked")
	public void testSelectReturnsItemsSelectedByTheGivenFilter() {
		List in = Coercions.list(new Object(), new Object());
		List out = Coercions.list(in.get(0));

		Mock mockFilter = mock(Filter.class);
		mockFilter.expects(once()).method("execute").with(eq(in.get(0))).will(returnValue(true));
		mockFilter.expects(once()).method("execute").with(eq(in.get(1))).will(returnValue(false));

		assertEquals(out, select(in, (Filter) mockFilter.proxy()));
	}

    @SuppressWarnings("unchecked")
    public void testRejectReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
        List rejected = reject(Coercions.list(), (Filter) mock(Filter.class).proxy());
        assertTrue(rejected.isEmpty());
    }

    @SuppressWarnings("unchecked")
	public void testRejectReturnsItemsSelectedByTheGivenFilter() {
		List in = Coercions.list(new Object(), new Object());
		List out = Coercions.list(in.get(0));

		Mock mockFilter = mock(Filter.class);
		mockFilter.expects(once()).method("execute").with(eq(in.get(1))).will(returnValue(true));
		mockFilter.expects(once()).method("execute").with(eq(in.get(0))).will(returnValue(false));

		assertEquals(out, reject(in, (Filter) mockFilter.proxy()));
	}

    @SuppressWarnings("unchecked")
	public void testForEachAppliesCommandToEachItemInGivenCollection() {
		List in = Coercions.list(new Object(), new Object());

		Mock mockCommand = mock(Command.class);
		mockCommand.expects(once()).method("execute").with(same(in.get(0)));
		mockCommand.expects(once()).method("execute").with(same(in.get(1)));

		forEach(in, (Command) mockCommand.proxy());
	}

	@SuppressWarnings("unchecked")
	public void testHeadReturnsTheFirstItemOfTheGivenList() {
		List in = Coercions.list(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
		assertSame(in.get(0), head(in));
	}

	@SuppressWarnings("unchecked")
	public void testHeadReturnsOneOfTheItemsInANonListCollection() {
		Set in = Coercions.set(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
		assertTrue(in.contains(head(in)));
	}

	public void testHeadOrNullIfEmptyReturnsNullIfTheCollectionIsEmpty() {
		assertNull(headOrNullIfEmpty(Coercions.list()));
	}
    
    public void testHeadOrDefaultIfEmptyReturnsDefaultIfTheCollectionIsEmpty() {
        assertEquals(FOO, headOrDefaultIfEmpty(Coercions.list(), FOO));
    }

    public void testHeadOrDefaultReturnsHeadIfTheCollectionIsNotEmpty() {
        assertSame(FOO, headOrDefaultIfEmpty(Coercions.list(FOO), BAR));
    }
    
    public void testHeadOrDefaultIfEmptyWithDifferentTypes() {
        final Number v = headOrDefaultIfEmpty(Coercions.list(DOUBLE_TWO), INTEGER_ONE);
        assertSame(DOUBLE_TWO, v);
    }

	public void testJoin() {
		assertEquals("a,b,c", join(Coercions.list("a", "b", "c"), ","));
		assertEquals("1-2-3", join(Coercions.list(1, 2, 3), "-"));
	}

    public void testSplit() {
        assertEquals(list("a", "b", "c"), split("a,b,c", ","));
        assertEquals(list("a,b,c"), split("a,b,c", ";"));
    }
    
    @SuppressWarnings("unchecked")
	public void testGroupWithEmptyCollectionReturnsAnEmptyList() {
		assertTrue(group(Coercions.set(), (Functor) Dummy.newDummy(Functor.class)).isEmpty());
	}

	@SuppressWarnings("unchecked")
	public void testGroupPlacesValuesWithEqualKeysIntoTheSameGroup() {
		final Object key = new Object();

		Mock functorMock = mock(Functor.class);
		functorMock.stubs().method("execute").will(returnValue(key));

		Map<Object, List<Integer>> groups = group(Coercions.list(1, 2, 3), (Functor<Integer, Object>) functorMock.proxy());
		assertEquals("size", 1, groups.size());
		assertEquals("key", key, head(groups.keySet()));

		assertEquals("values", Coercions.list(1, 2, 3), head(groups.values()));
	}

	@SuppressWarnings("unchecked")
	public void testGroupPlacesValuesWithUnequalKeysIntoDifferentGroups() {
		setUpFunctorExpectations(Coercions.list(1, 2, 3), Coercions.list("a", "a", "b"));

		Map<String, List<Integer>> groups = group(Coercions.list(1, 2, 3), (Functor<Integer, String>) mockFunctor.proxy());
		assertEquals("keys", Coercions.set("a", "b"), groups.keySet());

		assertEquals("a values", Coercions.list(1, 2), groups.get("a"));
		assertEquals("b values", Coercions.list(3), groups.get("b"));
	}

	@SuppressWarnings("unchecked")
	public void testAppend() {
		assertEquals(Coercions.list(1.0, 2, 2, "a"), append(Coercions.list(1.0, 2), Coercions.list(2, "a")));
	}

	public void testListTabulate() throws Exception {
		Functor<Integer, Integer> functor = new Functor<Integer, Integer>() {
			public Integer execute(Integer value) {
				return value + 1;
			}
		};
		List<Integer> result = listTabulate(4, functor);
		List<Integer> expected = Coercions.list(1, 2, 3, 4);

		assertEquals(expected, result);
	}

	public void testTake() throws Exception {
		List<Integer> result = take(2, Coercions.list(1, 2, 3, 4));
		assertEquals(Coercions.list(1, 2), result);
	}

	public void testTakeRight() throws Exception {
		List<Integer> result = takeRight(3, Coercions.list(1, 2, 3, 4, 5));
		assertEquals(Coercions.list(3, 4, 5), result);
	}

	public void testDrop() throws Exception {
		List<Integer> result = drop(2, Coercions.list(1, 2, 3, 4));
		assertEquals(Coercions.list(3, 4), result);
	}

	public void testDropRight() throws Exception {
		List<Integer> result = dropRight(2, Coercions.list(1, 2, 3, 4, 5));
		assertEquals(Coercions.list(1, 2, 3), result);
	}

	@SuppressWarnings("unchecked")
	public void testZip() throws Exception {
		List list = Coercions.list(Coercions.list("one", "two", "three"), Coercions.list(1, 2, 3), Coercions.list(true, false, true, false));
		List expected = Coercions.list(Coercions.list("one", 1, true), Coercions.list("two", 2, false), Coercions.list("three", 3, true));
		assertEquals(expected, zip(list));
	}

	@SuppressWarnings("unchecked")
    public void testShortestWithSameTypeArguments() {
		List<Integer> shortest = Coercions.list(1, 2);
		Collection<List<Integer>> list = Coercions.list(Coercions.list(1, 2, 3), shortest, Coercions.list(1, 2, 3, 4));
        List<Integer> result = shortest(list);
		assertEquals(shortest, result);
	}
	
	@SuppressWarnings("unchecked")
    public void testShortestWithDifferentCollectionTypes() {
	    List<Integer> shortest = Coercions.list(1, 2);
        List<Collection<Integer>> list = Coercions.list(Coercions.list(1, 2, 3), shortest, Coercions.set(1, 2, 3, 4));
        Collection<Integer> result = shortest(list);
	    assertEquals(shortest, result);
	}

    @SuppressWarnings("unchecked")
    public void testShortestWithDifferentCollectedTypes() {
	    final List<Integer> first = Coercions.list(1, 2, 3);
	    Collection<? extends Number> result = shortest(Coercions.list(first, Coercions.list(1, 2), Coercions.list(1.0, 2, 3, 4)));
	    assertEquals(Coercions.list(1, 2), result);
	}
    
    @SuppressWarnings("unchecked")
    public void testShortestWithDifferentCollectionAndCollectedTypes() {
        List<Integer> shortest = Coercions.list(1, 2);
        Collection<? extends Number> result = shortest(Coercions.list(Coercions.set(1, 2, 3), shortest, Coercions.list(1.0, 2, 3, 4)));
        assertEquals(shortest, result);
    }

	@SuppressWarnings("unchecked")
	public void testLongest() throws Exception {
		List<Boolean> longest = Coercions.list(true, false, true, false);
		List list = Coercions.list(Coercions.list("one", "two", "three"), Coercions.list(1, 2), longest);
		assertEquals(longest, longest(list));
	}

	@SuppressWarnings("unchecked")
	public void testSlice() {
		List list = Coercions.list(Coercions.list("one", "two", "three"), Coercions.list(1, 2, 3), Coercions.list(true, false, true));
		assertEquals(Coercions.list("two", 2, false), slice(1, list));
	}

	public void testReverse() throws Exception {
		List<Integer> toReverse = Coercions.list(1, 2, 3);
		List<Integer> result = reverse(toReverse);
		assertEquals(Coercions.list(3, 2, 1), result);
		assertNotSame(result, toReverse);
	}

	public void testFoldToAddSomeNumbers() {
		Functor2<Integer, Integer, Integer> summer = new Functor2<Integer, Integer, Integer>() {
			public Integer execute(Integer accumulator, Integer value) {
				return accumulator + value;
			}
		};
		assertEquals(10, fold(0, Coercions.list(1, 2, 3, 4), summer).intValue());
		assertEquals(11, fold(1, Coercions.list(1, 2, 3, 4), summer).intValue());
	}

	public void testFoldToReverseList() {
		Functor2<List<Integer>, Integer, List<Integer>> reverser = new Functor2<List<Integer>, Integer, List<Integer>>() {
			public List<Integer> execute(List<Integer> accumulator, Integer value) {
				accumulator.add(0, value);
				return accumulator;
			}
		};
		assertEquals(Coercions.list(4, 3, 2, 1), fold(new ArrayList<Integer>(), Coercions.list(1, 2, 3, 4), reverser));
	}
	
	@SuppressWarnings("unchecked")
	public void testSequenceExecutesCommandsInOrder() throws Exception {
		Mock first = mock(Command.class);
		Mock second = mock(Command.class);
		first.expects(once()).method("execute").id("first");
		second.expects(once()).method("execute").after(first, "first");
		
		Command sequence = sequence((Command) first.proxy(), (Command) second.proxy());
		sequence.execute(null);
	}

	@SuppressWarnings("unchecked")
	public void testHeadOrDefaultIfEmptyWhenEmpty() {
		assertEquals("default", headOrDefaultIfEmpty(Collections.EMPTY_LIST, "default"));
	}

	public void testHeadOrDefaultIfEmptyWhenNotEmpty() {
		assertEquals("foo", headOrDefaultIfEmpty(Coercions.list("foo"), "default"));
	}
}
