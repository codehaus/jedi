package jedi.functional;

import static java.util.Collections.emptyList;
import static jedi.functional.Box.boxInts;
import static jedi.functional.Coercions.asList;
import static jedi.functional.Coercions.list;
import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.drop;
import static jedi.functional.FunctionalPrimitives.dropRight;
import static jedi.functional.FunctionalPrimitives.flatten;
import static jedi.functional.FunctionalPrimitives.fold;
import static jedi.functional.FunctionalPrimitives.forEach;
import static jedi.functional.FunctionalPrimitives.group;
import static jedi.functional.FunctionalPrimitives.head;
import static jedi.functional.FunctionalPrimitives.headOption;
import static jedi.functional.FunctionalPrimitives.headOrDefaultIfEmpty;
import static jedi.functional.FunctionalPrimitives.headOrNullIfEmpty;
import static jedi.functional.FunctionalPrimitives.join;
import static jedi.functional.FunctionalPrimitives.last;
import static jedi.functional.FunctionalPrimitives.lastOption;
import static jedi.functional.FunctionalPrimitives.lastOrDefaultIfEmpty;
import static jedi.functional.FunctionalPrimitives.lastOrNullIfEmpty;
import static jedi.functional.FunctionalPrimitives.listTabulate;
import static jedi.functional.FunctionalPrimitives.longest;
import static jedi.functional.FunctionalPrimitives.only;
import static jedi.functional.FunctionalPrimitives.pop;
import static jedi.functional.FunctionalPrimitives.popOption;
import static jedi.functional.FunctionalPrimitives.produce;
import static jedi.functional.FunctionalPrimitives.reject;
import static jedi.functional.FunctionalPrimitives.reverse;
import static jedi.functional.FunctionalPrimitives.select;
import static jedi.functional.FunctionalPrimitives.sequence;
import static jedi.functional.FunctionalPrimitives.shortest;
import static jedi.functional.FunctionalPrimitives.slice;
import static jedi.functional.FunctionalPrimitives.split;
import static jedi.functional.FunctionalPrimitives.tabulate;
import static jedi.functional.FunctionalPrimitives.tail;
import static jedi.functional.FunctionalPrimitives.take;
import static jedi.functional.FunctionalPrimitives.takeMiddle;
import static jedi.functional.FunctionalPrimitives.takeRight;
import static jedi.functional.FunctionalPrimitives.zip;
import static jedi.option.Options.None;
import static jedi.option.Options.Some;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jmock.Mock;
import org.jmock.util.Dummy;
@SuppressWarnings("unchecked")
public class FunctionalPrimitivesTest extends ClosureTestCase {

    public void testAppend() {
        assertEquals(list(1.0, 2, 2, "a"), append(list(1.0, 2), list(2, "a")));
    }

    public void testCollectReturnsAListOfItemsMappedByTheGivenFunctor() {
        final List<Object> in = list(new Object(), new Object());
        final List<Object> expectedOut = list(new Object(), new Object());

        assertEquals(expectedOut, collect(in, setUpFunctorExpectations(in, expectedOut)));
    }

    public void testCollectReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
        final List list = collect(list(), functor);
        assertTrue(list.isEmpty());
    }

    public void testDrop() throws Exception {
        final List<Integer> result = drop(2, list(1, 2, 3, 4));
        assertEquals(list(3, 4), result);
    }

    public void testDropRight() throws Exception {
        final List<Integer> result = dropRight(2, list(1, 2, 3, 4, 5));
        assertEquals(list(1, 2, 3), result);
    }

    public void testFlattenReturnsAggregatedList() {
        final List inputValues = list(1, 2);
        setUpFunctorExpectations(inputValues, list(list("A", "B"), list("C", "D")));
        assertEquals(list("A", "B", "C", "D"), flatten(inputValues, functor));
    }

    public void testFlattenReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
        assertTrue(flatten(list(), functor).isEmpty());
    }

    public void testFoldToAddSomeNumbers() {
        final Functor2<Integer, Integer, Integer> summer = new Functor2<Integer, Integer, Integer>() {
            public Integer execute(Integer accumulator, Integer value) {
                return accumulator + value;
            }
        };
        assertEquals(10, fold(0, list(1, 2, 3, 4), summer).intValue());
        assertEquals(11, fold(1, list(1, 2, 3, 4), summer).intValue());
    }

    public void testFoldToReverseList() {
        final Functor2<List<Integer>, Integer, List<Integer>> reverser = new Functor2<List<Integer>, Integer, List<Integer>>() {
            public List<Integer> execute(List<Integer> accumulator, Integer value) {
                accumulator.add(0, value);
                return accumulator;
            }
        };
        assertEquals(list(4, 3, 2, 1), fold(new ArrayList<Integer>(), list(1, 2, 3, 4), reverser));
    }

    public void testForEachAppliesCommandToEachItemInGivenCollection() {
        final List in = list(new Object(), new Object());

        final Mock mockCommand = mock(Command.class);
        mockCommand.expects(once()).method("execute").with(same(in.get(0)));
        mockCommand.expects(once()).method("execute").with(same(in.get(1)));

        forEach(in, (Command) mockCommand.proxy());
    }

    public void testGroupPlacesValuesWithEqualKeysIntoTheSameGroup() {
        final Object key = new Object();

        final Mock functorMock = mock(Functor.class);
        functorMock.stubs().method("execute").will(returnValue(key));

        final Map<Object, List<Integer>> groups = group(list(1, 2, 3), (Functor<Integer, Object>) functorMock.proxy());
        assertEquals("size", 1, groups.size());
        assertEquals("key", key, head(groups.keySet()));

        assertEquals("values", list(1, 2, 3), head(groups.values()));
    }

    public void testGroupPlacesValuesWithUnequalKeysIntoDifferentGroups() {
        setUpFunctorExpectations(list(1, 2, 3), list("a", "a", "b"));

        final Map<String, List<Integer>> groups = group(list(1, 2, 3), (Functor<Integer, String>) mockFunctor.proxy());
        assertEquals("keys", Coercions.set("a", "b"), groups.keySet());

        assertEquals("a values", list(1, 2), groups.get("a"));
        assertEquals("b values", list(3), groups.get("b"));
    }

    public void testGroupWithEmptyCollectionReturnsAnEmptyList() {
        assertTrue(group(Coercions.set(), (Functor) Dummy.newDummy(Functor.class)).isEmpty());
    }

    public void testHeadOrDefaultIfEmptyWhenEmpty() {
        assertEquals(FOO, headOrDefaultIfEmpty(Collections.EMPTY_LIST, FOO));
    }

    public void testHeadOrDefaultIfEmptyWhenNotEmpty() {
        assertEquals(FOO, headOrDefaultIfEmpty(list(FOO), BAR));
    }

    public void testHeadOrDefaultIfEmptyWithDifferentTypes() {
        final Number v = headOrDefaultIfEmpty(list(DOUBLE_TWO), INTEGER_ONE);
        assertSame(DOUBLE_TWO, v);
    }

    public void testHeadOrNullIfEmptyReturnsNullIfTheCollectionIsEmpty() {
        assertNull(headOrNullIfEmpty(list()));
    }
    
	public void testHeadOptionWithEmptyCollection() {
		assertEquals(None(), headOption(emptyList()));
	}

	public void testHeadOptionWithNonEmptyCollection() {
		assertEquals(Some("a"), headOption(list("a", "b", "c")));
	}

    public void testHeadReturnsOneOfTheItemsInANonListCollection() {
        final Set in = Coercions.set(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
        assertTrue(in.contains(head(in)));
    }

    public void testHeadReturnsTheFirstItemOfTheGivenList() {
        final List in = list(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
        assertSame(in.get(0), head(in));
    }

    public void testLastOrDefaultIfEmptyWhenEmpty() {
        assertEquals(FOO, lastOrDefaultIfEmpty(Collections.EMPTY_LIST, FOO));
    }

    public void testLastOrDefaultIfEmptyWhenNotEmpty() {
        assertEquals(FOO, lastOrDefaultIfEmpty(list(FOO), BAR));
    }

    public void testLastOrDefaultIfEmptyWithDifferentTypes() {
        final Number v = lastOrDefaultIfEmpty(list(DOUBLE_TWO), INTEGER_ONE);
        assertSame(DOUBLE_TWO, v);
    }

    public void testLastOrNullIfEmptyReturnsNullIfTheCollectionIsEmpty() {
        assertNull(lastOrNullIfEmpty(list()));
    }
    
	public void testLastOptionWithEmptyList() {
		assertEquals(None(), lastOption(emptyList()));
	}

	public void testLastOptionWithNonEmptyList() {
		assertEquals(Some("c"), lastOption(list("a", "b", "c")));
	}

    public void testLastReturnsOneOfTheItemsInANonListCollection() {
        final Set in = Coercions.set(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
        assertTrue(in.contains(last(in)));
    }

    public void testLastReturnsTheLastItemOfTheGivenList() {
        final List in = list(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
        assertSame(in.get(in.size() - 1), last(in));
    }

    public void testJoin() {
        assertEquals("a,b,c", join(list("a", "b", "c"), ","));
        assertEquals("1-2-3", join(list(1, 2, 3), "-"));
        assertEquals("123", join(list(1, 2, 3)));
    }

    public void testListTabulate() throws Exception {
        final Functor<Integer, Integer> functor = new Functor<Integer, Integer>() {
            public Integer execute(Integer value) {
                return value + 1;
            }
        };
        final List<Integer> result = listTabulate(4, functor);
        final List<Integer> expected = list(1, 2, 3, 4);

        assertEquals(expected, result);
    }

    public void testListWithDifferentTypes() {
        final List< ? extends Number> list = list(INTEGER_ONE, DOUBLE_TWO);
        assertSame(list.get(0), INTEGER_ONE);
        assertSame(list.get(1), DOUBLE_TWO);
    }

    public void testLongest() throws Exception {
        final List<Boolean> longest = list(true, false, true, false);
        final List list = list(list("one", "two", "three"), list(1, 2), longest);
        assertEquals(longest, longest(list));
    }

    public void testOnlyReturnsTheHeadOfASingleElementCollection() {
        final List<Object> list = list(new Object());
        assertSame(head(list), only(list));
    }

    public void testOnlyThrowsAnAssertionErrorIfTheCollectionContainsMoreThanOneElement() {
        try {
            only(list(1, 2));
            fail();
        } catch (final jedi.assertion.AssertionError e) {
            // As expected
        }
    }

    public void testOnlyThrowsAnAssertionErrorIfTheCollectionContainsNoElements() {
        try {
            only(list());
            fail();
        } catch (final jedi.assertion.AssertionError e) {
            // As expected
        }
    }

    public void testRejectReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
        final List rejected = reject(list(), (Filter) mock(Filter.class).proxy());
        assertTrue(rejected.isEmpty());
    }

    public void testRejectReturnsItemsSelectedByTheGivenFilter() {
        final List in = list(new Object(), new Object());
        final List out = list(in.get(0));

        final Mock mockFilter = mock(Filter.class);
        mockFilter.expects(once()).method("execute").with(eq(in.get(1))).will(returnValue(true));
        mockFilter.expects(once()).method("execute").with(eq(in.get(0))).will(returnValue(false));

        assertEquals(out, reject(in, (Filter) mockFilter.proxy()));
    }

    public void testReverse() throws Exception {
        final List<Integer> toReverse = list(1, 2, 3);
        final List<Integer> result = reverse(toReverse);
        assertEquals(list(3, 2, 1), result);
        assertNotSame(result, toReverse);
    }

    public void testSelectReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
        final List selected = select(list(), (Filter) mock(Filter.class).proxy());
        assertTrue(selected.isEmpty());
    }

    public void testSelectReturnsItemsSelectedByTheGivenFilter() {
        final List in = list(new Object(), new Object());
        final List out = list(in.get(0));

        final Mock mockFilter = mock(Filter.class);
        mockFilter.expects(once()).method("execute").with(eq(in.get(0))).will(returnValue(true));
        mockFilter.expects(once()).method("execute").with(eq(in.get(1))).will(returnValue(false));

        assertEquals(out, select(in, (Filter) mockFilter.proxy()));
    }

    public void testSequenceExecutesCommandsInOrder() throws Exception {
        final Mock first = mock(Command.class);
        final Mock second = mock(Command.class);
        first.expects(once()).method("execute").id("first");
        second.expects(once()).method("execute").after(first, "first");

        final Command sequence = sequence((Command) first.proxy(), (Command) second.proxy());
        sequence.execute(null);
    }

    public void testShortestWithDifferentCollectedTypes() {
        final List<Integer> first = list(1, 2, 3);
        final Collection< ? extends Number> result = shortest(list(first, list(1, 2), list(1.0, 2, 3, 4)));
        assertEquals(list(1, 2), result);
    }

    public void testShortestWithDifferentCollectionAndCollectedTypes() {
        final List<Integer> shortest = list(1, 2);
        final Collection< ? extends Number> result = shortest(list(Coercions.set(1, 2, 3), shortest, list(1.0, 2, 3, 4)));
        assertEquals(shortest, result);
    }

    public void testShortestWithDifferentCollectionTypes() {
        final List<Integer> shortest = list(1, 2);
        final List<Collection<Integer>> list = list(list(1, 2, 3), shortest, Coercions.set(1, 2, 3, 4));
        final Collection<Integer> result = shortest(list);
        assertEquals(shortest, result);
    }

    public void testShortestWithSameTypeArguments() {
        final List<Integer> shortest = list(1, 2);
        final Collection<List<Integer>> list = list(list(1, 2, 3), shortest, list(1, 2, 3, 4));
        final List<Integer> result = shortest(list);
        assertEquals(shortest, result);
    }

    public void testSlice() {
        final List list = list(list("one", "two", "three"), list(1, 2, 3), list(true, false, true));
        assertEquals(list("two", 2, false), slice(1, list));
    }

    public void testSplit() {
        assertEquals(list("a", "b", "c"), split("a,b,c", ","));
        assertEquals(list("a,b,c"), split("a,b,c", ";"));
    }

    public void testTailReturnsAllItemsExceptTheFirst() {
        assertEquals(list("b", "c"), tail(list("a", "b", "c")));
        assertEquals(list("b"), tail(list("a", "b")));
        assertEquals(list(), tail(list("a")));
    }

    public void testTake() throws Exception {
        final List<Integer> result = take(2, list(1, 2, 3, 4));
        assertEquals(list(1, 2), result);
    }

    public void testTakeRight() throws Exception {
        final List<Integer> result = takeRight(3, list(1, 2, 3, 4, 5));
        assertEquals(list(3, 4, 5), result);
    }

    public void testTakeMiddle() {
    	assertEquals(list(2,3,4), takeMiddle(1, 3, list(1,2,3,4,5)));
    }

	public void testTabulate() throws Exception {
		List<Integer> line = list(1,1,1,2,2,2);
		List<List<Integer>> result = list(list(1,1,1), list(2,2,2));
		assertEquals(result, tabulate(line, 3));
	}

    public void testTabulateWithEmptyList() throws Exception {
    	assertEquals(Collections.EMPTY_LIST, tabulate(Collections.EMPTY_LIST, 3));
    }

    public void testTabulateThrowsAssertionErrorIfTheGivenListIsTheWrongLength() throws Exception {
    	try {
    		tabulate(list(1,1,2), 2);
		} catch (jedi.assertion.AssertionError expected) {
			assertEquals("line.size must be a multiple of length: context {[[1, 1, 2]], [2]}", expected.getMessage());
		}
    }

    public void testZip() throws Exception {
        final List list = list(list("one", "two", "three"), list(1, 2, 3), list(true, false, true, false));
        final List expected = list(list("one", 1, true), list("two", 2, false), list("three", 3, true));
        assertEquals(expected, zip(list));
    }
    
    public void testPop() {
        List<String> list = list("foo", "bar", "baz");
        assertEquals("baz", pop(list));
        assertEquals(list("foo", "bar"), list);
    }
    
	public void testPopOptionWithEmptyList() {
		assertEquals(None(), popOption(emptyList()));
	}

	public void testPopOptionWithNonEmptyList() {
		assertEquals(Some("b"), popOption(list("a", "b")));
	}

	public void testProduce() {
		Functor2<Integer, String, String> factory = new Functor2<Integer, String, String>() {
			public String execute(Integer t, String u) {
				return t + u;
			}
		};
		Collection<Integer> left = asList(boxInts(1,2));
		Collection<String> right = list("a","b");
		List<String> result = produce(left, right, factory);
		assertEquals(list("1a","1b", "2a", "2b"), result);
	}

}
