package jedi.functional;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.emptyList;
import static jedi.functional.Box.boxInts;
import static jedi.functional.Coercions.asList;
import static jedi.functional.Coercions.list;
import static jedi.functional.Coercions.set;
import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.drop;
import static jedi.functional.FunctionalPrimitives.dropRight;
import static jedi.functional.FunctionalPrimitives.first;
import static jedi.functional.FunctionalPrimitives.firstOption;
import static jedi.functional.FunctionalPrimitives.firstOrDefault;
import static jedi.functional.FunctionalPrimitives.firstOrNull;
import static jedi.functional.FunctionalPrimitives.flatten;
import static jedi.functional.FunctionalPrimitives.fold;
import static jedi.functional.FunctionalPrimitives.foldPowerset;
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
import static jedi.functional.FunctionalPrimitives.partition;
import static jedi.functional.FunctionalPrimitives.pop;
import static jedi.functional.FunctionalPrimitives.popOption;
import static jedi.functional.FunctionalPrimitives.powerset;
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
import static jedi.functional.FunctionalPrimitives.zipWithIndex;
import static jedi.option.Options.none;
import static jedi.option.Options.some;
import static jedi.tuple.Tuples.pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jedi.assertion.AssertionError;
import jedi.filters.AllPassFilter;
import jedi.tuple.Tuple2;

import org.jmock.Mock;
import org.jmock.util.Dummy;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class FunctionalPrimitivesTest extends ClosureTestCase {

	@Test
	public void testAppend() {
		List<? extends Number> listA = list(1.0, 2);
		List<? extends Object> listB = list(2, "a");
		List<Object> result = append(listA, listB);
		assertEquals(list(1.0, 2, 2, "a"), result);
		assertNotSame(result, listA, listB);
	}

	@Test
	public void testCollectReturnsAListOfItemsMappedByTheGivenFunctor() {
		final List<Object> in = list(new Object(), new Object());
		final List<Object> expectedOut = list(new Object(), new Object());

		assertEquals(expectedOut, collect(in, setUpFunctorExpectations(in, expectedOut)));
	}

	@Test
	public void testCollectReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
		final List list = collect(list(), functor);
		assertTrue(list.isEmpty());
	}

	@Test
	public void testDrop() throws Exception {
		List<Integer> in = list(1, 2, 3, 4);
		final List<Integer> result = drop(2, in);
		assertEquals(list(3, 4), result);
		assertNotSame(in, result);
	}

	@Test
	public void testDropRight() throws Exception {
		List<Integer> in = list(1, 2, 3, 4, 5);
		List<Integer> result = dropRight(2, in);
		assertEquals(list(1, 2, 3), result);
		assertNotSame(in, result);
	}

	@Test
	public void testFlattenReturnsAggregatedList() {
		final List inputValues = list(1, 2);
		setUpFunctorExpectations(inputValues, list(list("A", "B"), list("C", "D")));
		List result = flatten(inputValues, functor);
		assertEquals(list("A", "B", "C", "D"), result);
		assertNotSame(inputValues, result);
	}

	@Test
	public void testFlattenReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
		List<Object> in = list();
		List flattened = flatten(in, functor);
		assertTrue(flattened.isEmpty());
		assertNotSame(in, flattened);
	}

	@Test
	public void testFoldToAddSomeNumbers() {
		final Functor2<Integer, Integer, Integer> summer = new Functor2<Integer, Integer, Integer>() {
			public Integer execute(Integer accumulator, Integer value) {
				return accumulator + value;
			}
		};
		assertEquals(10, fold(0, list(1, 2, 3, 4), summer).intValue());
		assertEquals(11, fold(1, list(1, 2, 3, 4), summer).intValue());
	}

	@Test
	public void testFoldToReverseList() {
		final Functor2<List<Integer>, Integer, List<Integer>> reverser = new Functor2<List<Integer>, Integer, List<Integer>>() {
			public List<Integer> execute(List<Integer> accumulator, Integer value) {
				accumulator.add(0, value);
				return accumulator;
			}
		};
		List<Integer> expected = list(4, 3, 2, 1);
		List<Integer> reversed = fold(new ArrayList<Integer>(), list(1, 2, 3, 4), reverser);
		assertEquals(expected, reversed);
		assertNotSame(expected, reversed);
	}

	@Test
	public void testForEachAppliesCommandToEachItemInGivenCollection() {
		final List in = list(new Object(), new Object());

		final Mock mockCommand = mock(Command.class);
		mockCommand.expects(once()).method("execute").with(same(in.get(0)));
		mockCommand.expects(once()).method("execute").with(same(in.get(1)));

		forEach(in, (Command) mockCommand.proxy());
	}

	@Test
	public void testGroupPlacesValuesWithEqualKeysIntoTheSameGroup() {
		final Object key = new Object();

		final Mock functorMock = mock(Functor.class);
		functorMock.stubs().method("execute").will(returnValue(key));

		final Map<Object, List<Integer>> groups = group(list(1, 2, 3), (Functor<Integer, Object>) functorMock.proxy());
		assertEquals("size", 1, groups.size());
		assertEquals("key", key, head(groups.keySet()));

		assertEquals("values", list(1, 2, 3), head(groups.values()));
	}

	@Test
	public void testGroupPlacesValuesWithUnequalKeysIntoDifferentGroups() {
		setUpFunctorExpectations(list(1, 2, 3), list("a", "a", "b"));

		final Map<String, List<Integer>> groups = group(list(1, 2, 3), (Functor<Integer, String>) mockFunctor.proxy());
		assertEquals("keys", set("a", "b"), groups.keySet());

		assertEquals("a values", list(1, 2), groups.get("a"));
		assertEquals("b values", list(3), groups.get("b"));
	}

	@Test
	public void testGroupWithEmptyCollectionReturnsAnEmptyList() {
		assertTrue(group(set(), (Functor) Dummy.newDummy(Functor.class)).isEmpty());
	}

	@Test
	public void testHeadOrDefaultIfEmptyWhenEmpty() {
		assertEquals(FOO, headOrDefaultIfEmpty(EMPTY_LIST, FOO));
	}

	@Test
	public void testHeadOrDefaultIfEmptyWhenNotEmpty() {
		assertEquals(FOO, headOrDefaultIfEmpty(list(FOO), BAR));
	}

	@Test
	public void testHeadOrDefaultIfEmptyWithDifferentTypes() {
		final Number v = headOrDefaultIfEmpty(list(DOUBLE_TWO), INTEGER_ONE);
		assertSame(DOUBLE_TWO, v);
	}

	@Test
	public void testHeadOrNullIfEmptyReturnsNullIfTheCollectionIsEmpty() {
		assertNull(headOrNullIfEmpty(list()));
	}

	@Test
	public void testHeadOptionWithEmptyCollection() {
		assertEquals(none(), headOption(emptyList()));
	}

	@Test
	public void testHeadOptionWithNonEmptyCollection() {
		assertEquals(some("a"), headOption(list("a", "b", "c")));
	}

	@Test
	public void testHeadReturnsOneOfTheItemsInANonListCollection() {
		final Set in = Coercions.set(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
		assertTrue(in.contains(head(in)));
	}

	@Test
	public void testHeadReturnsTheFirstItemOfTheGivenList() {
		final List in = list(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
		assertSame(in.get(0), head(in));
	}

	@Test
	public void testLastOrDefaultIfEmptyWhenEmpty() {
		assertEquals(FOO, lastOrDefaultIfEmpty(EMPTY_LIST, FOO));
	}

	@Test
	public void testLastOrDefaultIfEmptyWhenNotEmpty() {
		assertEquals(FOO, lastOrDefaultIfEmpty(list(FOO), BAR));
	}

	@Test
	public void testLastOrDefaultIfEmptyWithDifferentTypes() {
		final Number v = lastOrDefaultIfEmpty(list(DOUBLE_TWO), INTEGER_ONE);
		assertSame(DOUBLE_TWO, v);
	}

	@Test
	public void testLastOrNullIfEmptyReturnsNullIfTheCollectionIsEmpty() {
		assertNull(lastOrNullIfEmpty(list()));
	}

	@Test
	public void testLastOptionWithEmptyList() {
		assertEquals(none(), lastOption(emptyList()));
	}

	@Test
	public void testLastOptionWithNonEmptyList() {
		assertEquals(some("c"), lastOption(list("a", "b", "c")));
	}

	@Test
	public void testLastReturnsOneOfTheItemsInANonListCollection() {
		final Set in = Coercions.set(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
		assertTrue(in.contains(last(in)));
	}

	@Test
	public void testLastReturnsTheLastItemOfTheGivenList() {
		final List in = list(FOO, BAR, INTEGER_ONE, DOUBLE_TWO);
		assertSame(in.get(in.size() - 1), last(in));
	}

	@Test
	public void testJoin() {
		assertEquals("a,b,c", join(list("a", "b", "c"), ","));
		assertEquals("1-2-3", join(list(1, 2, 3), "-"));
		assertEquals("123", join(list(1, 2, 3)));
	}

	@Test
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

	@Test
	public void testListWithDifferentTypes() {
		final List<? extends Number> list = list(INTEGER_ONE, DOUBLE_TWO);
		assertSame(list.get(0), INTEGER_ONE);
		assertSame(list.get(1), DOUBLE_TWO);
	}

	@Test
	public void testLongest() throws Exception {
		final List<Boolean> longest = list(true, false, true, false);
		final List list = list(list("one", "two", "three"), list(1, 2), longest);
		assertEquals(longest, longest(list));
	}

	@Test
	public void testOnlyReturnsTheHeadOfASingleElementCollection() {
		final List<Object> list = list(new Object());
		assertSame(head(list), only(list));
	}

	@Test
	public void testOnlyThrowsAnAssertionErrorIfTheCollectionContainsMoreThanOneElement() {
		try {
			only(list(1, 2));
			fail();
		} catch (final jedi.assertion.AssertionError e) {
			// As expected
		}
	}

	@Test
	public void testOnlyThrowsAnAssertionErrorIfTheCollectionContainsNoElements() {
		try {
			only(list());
			fail();
		} catch (final jedi.assertion.AssertionError e) {
			// As expected
		}
	}

	@Test
	public void testRejectReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
		final List rejected = reject(list(), (Filter) mock(Filter.class).proxy());
		assertTrue(rejected.isEmpty());
	}

	@Test
	public void testRejectReturnsItemsSelectedByTheGivenFilter() {
		final List in = list(new Object(), new Object());
		final List out = list(in.get(0));

		final Mock mockFilter = mock(Filter.class);
		mockFilter.expects(once()).method("execute").with(eq(in.get(1))).will(returnValue(true));
		mockFilter.expects(once()).method("execute").with(eq(in.get(0))).will(returnValue(false));

		assertEquals(out, reject(in, (Filter) mockFilter.proxy()));
	}

	@Test
	public void testReverse() throws Exception {
		final List<Integer> toReverse = list(1, 2, 3);
		final List<Integer> result = reverse(toReverse);
		assertEquals(list(3, 2, 1), result);
		assertNotSame(result, toReverse);
	}

	@Test
	public void testSelectReturnsAnEmptyListWhenTheGivenCollectionIsEmpty() {
		final List selected = select(list(), (Filter) mock(Filter.class).proxy());
		assertTrue(selected.isEmpty());
	}

	@Test
	public void testSelectReturnsItemsSelectedByTheGivenFilter() {
		final List in = list(new Object(), new Object());
		final List out = list(in.get(0));

		final Mock mockFilter = mock(Filter.class);
		mockFilter.expects(once()).method("execute").with(eq(in.get(0))).will(returnValue(true));
		mockFilter.expects(once()).method("execute").with(eq(in.get(1))).will(returnValue(false));

		assertEquals(out, select(in, (Filter) mockFilter.proxy()));
		assertEquals("original list should be unchanged", 2, in.size());
	}

	@Test
	public void testSequenceExecutesCommandsInOrder() throws Exception {
		final Mock first = mock(Command.class);
		final Mock second = mock(Command.class);
		first.expects(once()).method("execute").id("first");
		second.expects(once()).method("execute").after(first, "first");

		final Command sequence = sequence((Command) first.proxy(), (Command) second.proxy());
		sequence.execute(null);
	}

	@Test
	public void testShortestWithDifferentCollectedTypes() {
		final List<Integer> first = list(1, 2, 3);
		final Collection<? extends Number> result = shortest(list(first, list(1, 2), list(1.0, 2, 3, 4)));
		assertEquals(list(1, 2), result);
	}

	@Test
	public void testShortestWithDifferentCollectionAndCollectedTypes() {
		final List<Integer> shortest = list(1, 2);
		final Collection<? extends Number> result = shortest(list(Coercions.set(1, 2, 3), shortest, list(1.0, 2, 3, 4)));
		assertEquals(shortest, result);
	}

	@Test
	public void testShortestWithDifferentCollectionTypes() {
		final List<Integer> shortest = list(1, 2);
		final List<Collection<Integer>> list = list(list(1, 2, 3), shortest, Coercions.set(1, 2, 3, 4));
		final Collection<Integer> result = shortest(list);
		assertEquals(shortest, result);
	}

	@Test
	public void testShortestWithSameTypeArguments() {
		final List<Integer> shortest = list(1, 2);
		final Collection<List<Integer>> list = list(list(1, 2, 3), shortest, list(1, 2, 3, 4));
		final List<Integer> result = shortest(list);
		assertEquals(shortest, result);
	}

	@Test
	public void testSlice() {
		final List list = list(list("one", "two", "three"), list(1, 2, 3), list(true, false, true));
		assertEquals(list("two", 2, false), slice(1, list));
	}

	@Test
	public void testSplit() {
		assertEquals(list("a", "b", "c"), split("a,b,c", ","));
		assertEquals(list("a,b,c"), split("a,b,c", ";"));
	}

	@Test
	public void testTailReturnsAllItemsExceptTheFirst() {
		List<String> in = list("a", "b", "c");
		assertEquals(list("b", "c"), tail(in));
		assertEquals("original list should not be changed", 3, in.size());

		assertEquals(list("b"), tail(list("a", "b")));
		assertEquals(list(), tail(list("a")));
	}

	@Test
	public void testTake() throws Exception {
		List<Integer> in = list(1, 2, 3, 4);
		final List<Integer> result = take(2, in);
		assertEquals(list(1, 2), result);
		assertEquals("original list should not be changed", 4, in.size());
	}

	@Test
	public void testTakeRight() throws Exception {
		List<Integer> in = list(1, 2, 3, 4, 5);
		final List<Integer> result = takeRight(3, in);
		assertEquals(list(3, 4, 5), result);
		assertEquals("original list should not be changed", 5, in.size());
	}

	@Test
	public void testTakeMiddle() {
		List<Integer> in = list(1, 2, 3, 4, 5);
		assertEquals(list(2, 3, 4), takeMiddle(1, 3, in));
		assertEquals("original list should not be changed", 5, in.size());
	}

	@Test
	public void testTabulate() throws Exception {
		List<Integer> line = list(1, 1, 1, 2, 2, 2);
		List<List<Integer>> result = list(list(1, 1, 1), list(2, 2, 2));
		assertEquals(result, tabulate(line, 3));
	}

	@Test
	public void testTabulateWithEmptyList() throws Exception {
		assertEquals(EMPTY_LIST, tabulate(EMPTY_LIST, 3));
	}

	@Test
	public void testTabulateThrowsAssertionErrorIfTheGivenListIsTheWrongLength() throws Exception {
		try {
			tabulate(list(1, 1, 2), 2);
		} catch (jedi.assertion.AssertionError expected) {
			assertEquals("size must be a multiple of length: context {[[1, 1, 2]], [2]}", expected.getMessage());
		}
	}

	@Test
	public void testZip() throws Exception {
		final List list = list(list("one", "two", "three"), list(1, 2, 3), list(true, false, true, false));
		final List expected = list(list("one", 1, true), list("two", 2, false), list("three", 3, true));
		assertEquals(expected, zip(list));
	}

	@Test
	public void testZipWithDifferentLengthLists() throws Exception {
		final List list = list(list("one", "two", "three"), list(1, 2, 3, 4, 5, 6), list(true, false, true, false, true));
		final List expected = list(list("one", 1, true), list("two", 2, false), list("three", 3, true));
		assertEquals(expected, zip(list));
	}

	@Test
	public void testZipToPair() throws Exception {
		final List<Tuple2<String, Integer>> expected = list(pair("one", 1), pair("two", 2), pair("three", 3));
		assertEquals(expected, zip(list("one", "two", "three"), list(1, 2, 3)));
	}

	@Test
	public void testZipToPairWithDifferentLengthLists() throws Exception {
		final List<Tuple2<String, Integer>> expected = list(pair("one", 1), pair("two", 2), pair("three", 3));
		assertEquals(expected, zip(list("one", "two", "three"), list(1, 2, 3, 4, 5)));
	}

	@Test
	public void testZipWithIndex() throws Exception {
		final List list = list("one", "two", "three");
		final List expected = list(pair("one", 0), pair("two", 1), pair("three", 2));
		assertEquals(expected, zipWithIndex(list));
	}

	@Test
	public void testPop() {
		List<String> list = list("foo", "bar", "baz");
		assertEquals("baz", pop(list));
		assertEquals(list("foo", "bar"), list);
	}

	@Test
	public void testPopOptionWithEmptyList() {
		assertEquals(none(), popOption(emptyList()));
	}

	@Test
	public void testPopOptionWithNonEmptyList() {
		assertEquals(some("b"), popOption(list("a", "b")));
	}

	@Test
	public void testProduce() {
		Functor2<Integer, String, String> factory = new Functor2<Integer, String, String>() {
			public String execute(Integer t, String u) {
				return t + u;
			}
		};
		Collection<Integer> left = asList(boxInts(1, 2));
		Collection<String> right = list("a", "b");
		List<String> result = produce(left, right, factory);
		assertEquals(list("1a", "1b", "2a", "2b"), result);
	}

	@Test
	public void testPartition() {
		Filter<Integer> filter = new Filter<Integer>() {
			public Boolean execute(Integer value) {
				return value.intValue() < 3;
			}
		};
		List<Integer> list = asList(boxInts(1, 2, 3, 4, 5));
		List<List<Integer>> expected = list(asList(boxInts(1, 2)), asList(boxInts(3, 4, 5)));
		assertEquals(expected, partition(list, filter));
		assertEquals(asList(boxInts(1, 2, 3, 4, 5)), list);
	}

	@Test
	public void testPartitionWithAllPassFilter() {
		List<Integer> list = asList(boxInts(1, 2, 3, 4, 5));
		List<List<Integer>> expected = list(list, Collections.<Integer> emptyList());
		assertEquals(expected, partition(list, new AllPassFilter<Integer>()));
	}

	@Test
	public void testPartitionWithNoPassFilter() {
		List<Integer> list = asList(boxInts(1, 2, 3, 4, 5));
		List<List<Integer>> expected = list(Collections.<Integer> emptyList(), list);
		assertEquals(expected, partition(list, FirstOrderLogic.not(new AllPassFilter<Integer>())));
	}

	@Test
	public void testPowersetReturnsEmptySetForEmptyInput() {
		Set output = powerset(EMPTY_LIST);

		assertEquals(1, output.size());
		assertTrue(output.contains(Collections.emptyList()));
	}

	@Test
	public void testPowersetReturnsPowersetForOneElement() {
		Set<List<String>> output = powerset(list("x"));

		assertEquals(2, output.size());
		assertTrue(output.contains(Collections.emptyList()));
		assertTrue(output.contains(list("x")));
	}

	@Test
	public void testPowersetReturnsPowersetForThreeElements() {
		Set<List<String>> output = powerset(list("x", "y", "z"));

		assertEquals(8, output.size());
		assertTrue(output.contains(Collections.emptyList()));
		assertTrue(output.contains(list("x")));
		assertTrue(output.contains(list("y")));
		assertTrue(output.contains(list("z")));
		assertTrue(output.contains(list("x", "y")));
		assertTrue(output.contains(list("x", "z")));
		assertTrue(output.contains(list("y", "z")));
		assertTrue(output.contains(list("x", "y", "z")));
	}

	private void assertNotSame(Object... objects) {
		Set<Object> set = set(objects);
		assertEquals("objects should be different", objects.length, set.size());
	}

	@Test
	public void testPowersetAppliesCommandToEmptyListGivenAnEmptyCollection() {
		Mock commandMock = mock(Command.class);
		commandMock.expects(once()).method("execute").with(eq(Collections.EMPTY_LIST));

		powerset(EMPTY_LIST, (Command) commandMock.proxy());
	}

	@Test
	public void testPowersetAppliesCommandToPowersetElementsGivenACollectionWithOneElement() {
		Mock commandMock = mock(Command.class);
		commandMock.expects(once()).method("execute").with(eq(Collections.EMPTY_LIST));
		commandMock.expects(once()).method("execute").with(eq(list("x")));

		powerset(list("x"), (Command) commandMock.proxy());
	}

	@Test
	public void testPowersetAppliesCommandToEachElementInThePowersetOfACollectionWithThreeElements() {
		Mock commandMock = mock(Command.class);
		commandMock.expects(once()).method("execute").with(eq(Collections.EMPTY_LIST));
		commandMock.expects(once()).method("execute").with(eq(list("x")));
		commandMock.expects(once()).method("execute").with(eq(list("y")));
		commandMock.expects(once()).method("execute").with(eq(list("z")));
		commandMock.expects(once()).method("execute").with(eq(list("x", "y")));
		commandMock.expects(once()).method("execute").with(eq(list("x", "z")));
		commandMock.expects(once()).method("execute").with(eq(list("y", "z")));
		commandMock.expects(once()).method("execute").with(eq(list("x", "y", "z")));

		powerset(list("x", "y", "z"), (Command) commandMock.proxy());
	}

	@Test
	public void testPowersetAppliesFunctorToEmptyListGivenAnEmptyCollectionAndReturnsTheFunctorsReturnValue() {
		Mock functorMock = mock(Functor2.class);
		functorMock.expects(once()).method("execute").with(eq("a"), eq(EMPTY_LIST)).will(returnValue("b"));

		Object result = foldPowerset("a", EMPTY_LIST, (Functor2) functorMock.proxy());
		assertEquals("b", result);
	}

	@Test
	public void testPowersetAppliesFunctorToPowersetElementsGivenACollectionWithOneElementAndReturnsTheAccumulatedValue() {
		Mock functorMock = mock(Functor2.class);
		functorMock.expects(once()).method("execute").with(eq("a"), eq(EMPTY_LIST)).will(returnValue("b"));
		functorMock.expects(once()).method("execute").with(eq("b"), eq(list("x"))).will(returnValue("c"));

		Object result = foldPowerset("a", list("x"), (Functor2) functorMock.proxy());
		assertEquals("c", result);
	}

	@Test
	public void testPowersetAppliesFunctorToEachElementInThePowersetOfACollectionWithThreeElementsAndReturnsTheAccumulatedValue() {
		Mock functorMock = mock(Functor2.class);
		functorMock.expects(once()).method("execute").with(eq("a"), eq(EMPTY_LIST)).will(returnValue("b"));
		functorMock.expects(once()).method("execute").with(eq("b"), eq(list("x"))).will(returnValue("c"));
		functorMock.expects(once()).method("execute").with(eq("c"), eq(list("x", "y"))).will(returnValue("d"));
		functorMock.expects(once()).method("execute").with(eq("d"), eq(list("x", "y", "z"))).will(returnValue("e"));
		functorMock.expects(once()).method("execute").with(eq("e"), eq(list("x", "z"))).will(returnValue("f"));
		functorMock.expects(once()).method("execute").with(eq("f"), eq(list("y"))).will(returnValue("g"));
		functorMock.expects(once()).method("execute").with(eq("g"), eq(list("y", "z"))).will(returnValue("h"));
		functorMock.expects(once()).method("execute").with(eq("h"), eq(list("z"))).will(returnValue("i"));

		Object returnValue = foldPowerset("a", list("x", "y", "z"), (Functor2) functorMock.proxy());
		assertEquals("i", returnValue);
	}

	@Test
	public void testFirstReturnsTheFirstElementFromTheCollectionMatchingTheFilter() {
		List<Integer> all = list(1,2,3,4);
		assertEquals(new Integer(2), first(all, greaterThan(1)));
	}

	@Test
	public void testFirstThrowsAnAssertionErrorIfNoElementMatchesTheFilter() {
		List<Integer> all = list(1,2,3,4);
		try {
			first(all, greaterThan(5));
			fail();
		} catch (AssertionError e) {
			// Expected
		}
	}

	@Test
	public void testFirstThrowsAnAssertionErrorIfIteratorIsEmpty() {
		try {
			first(Collections.EMPTY_LIST, greaterThan(5));
			fail();
		} catch (AssertionError e) {
			// Expected
		}
	}

	@Test
	public void testFirstOrDefaultReturnsTheFirstElementFromTheCollectionMatchingTheFilter() {
		List<Integer> all = list(1,2,3,4);
		assertEquals(new Integer(2), firstOrDefault(all, greaterThan(1), new Integer(5)));
	}

	@Test
	public void testFirstOrDefaultReturnsTheDefaultIfNoElementsMatch() {
		List<Integer> all = list(1,2,3,4);
		assertEquals(new Integer(5), firstOrDefault(all, greaterThan(4), new Integer(5)));
	}

	@Test
	public void testFirstOrNullReturnsTheFirstElementFromTheCollectionMatchingTheFilter() {
		List<Integer> all = list(1,2,3,4);
		assertEquals(new Integer(2), firstOrNull(all, greaterThan(1)));
	}

	@Test
	public void testFirstOrNullReturnsNullIfNoElementsMatch() {
		List<Integer> all = list(1,2,3,4);
		assertNull(firstOrNull(all, greaterThan(4)));
	}


	@Test
	public void testFirstOptionReturnsSomeContainingTheFirstElementFromTheCollectionMatchingTheFilter() {
		List<Integer> all = list(1,2,3,4);
		assertEquals(some(2), firstOption(all, greaterThan(1)));
	}

	@Test
	public void testFirstOptionReturnsNoneIfNoElementsMatch() {
		List<Integer> all = list(1,2,3,4);
		assertEquals(none(), firstOption(all, greaterThan(4)));
	}

	private Filter<Integer> greaterThan(final Integer boundary) {
		return new Filter<Integer>(){
			public Boolean execute(Integer value) {
				return value > boundary;
			}
		};
	}
}
