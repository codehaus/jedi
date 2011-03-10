package jedi.functional;

import static jedi.functional.Coercions.array;
import static jedi.functional.Coercions.asArray;
import static jedi.functional.Coercions.asFilter;
import static jedi.functional.Coercions.asFunctor;
import static jedi.functional.Coercions.asList;
import static jedi.functional.Coercions.asMap;
import static jedi.functional.Coercions.asSet;
import static jedi.functional.Coercions.cast;
import static jedi.functional.Coercions.list;
import static jedi.functional.Coercions.set;
import static jedi.functional.FunctionalPrimitives.makeList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jedi.JediTestCase;
import org.jmock.Mock;
import org.junit.Test;

public class CoercionsTest extends JediTestCase {
	@SuppressWarnings("unchecked")
	@Test
	public void testArrayWithMultipleClasses() {
		final Number[] a = Coercions.array(INTEGER_ONE, DOUBLE_TWO);

		assertSame(INTEGER_ONE, a[0]);
		assertSame(DOUBLE_TWO, a[1]);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAsFilter() {
		Mock functor = mock(Functor.class);
		Mock filter = mock(Filter.class);
		Filter asFilter = asFilter((Functor) functor.proxy(), (Filter) filter.proxy());

		functor.expects(once()).method("execute").with(ANYTHING).will(returnValue("functorResult"));
		filter.expects(once()).method("execute").with(eq("functorResult")).will(returnValue(true));

		assertEquals(true, asFilter.execute(ANYTHING));
	}

	@Test
	public void testArrayWithSingleClass() {
		final String[] a = array(FOO, BAR);
		assertSame(FOO, a[0]);
		assertSame(BAR, a[1]);
	}

	@Test
	public void testAsArray() {
		final List<String> list = list(FOO, BAR);
		final String[] a = asArray(list);
		assertSame(FOO, a[0]);
		assertSame(BAR, a[1]);
	}

	@Test
	public void testAsFunctorReturnsNullWhenKeyIsNotInMapAndAllowUncontainedKeysIsTrue() {
		verifyAsFunctorWithValidArgument(new Random().nextInt(), null, true, true);
	}

	@Test
	public void testAsFunctorReturnsValueFromMapWhenAllowUncontainedValuesIsFalse() {
		verifyAsFunctorWithValidArgument(new Random().nextInt(), null, true, false);
	}

	@Test
	public void testAsFunctorReturnsValueFromMapWhenAllowUncontainedValuesIsTrue() {
		final Random random = new Random();
		verifyAsFunctorWithValidArgument(random.nextInt(), random.nextInt(), true, true);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAsFunctorThrowsAnAssertionErrorWhenKeyIsNotInMapAndAllowUncontainedKeysIsFalse() {
		final int key = new Random().nextInt();
		final Mock mockMap = mock(Map.class);
		mockMap.stubs().method("containsKey").with(eq(key)).will(returnValue(false));

		final Functor functor = asFunctor((Map) mockMap.proxy(), false);
		try {
			functor.execute(key);
			fail();
		} catch (final jedi.assertion.AssertionError error) {
			// Expected
		}
	}

	@Test
	public void testAsList() {
		final Collection<String> collection = list(FOO, BAR);
		final List<String> expected = list(FOO, BAR);

		assertEquals(expected, asList(new String[] { FOO, BAR }));
		assertEquals(expected, asList(collection));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAsListWithDifferentClasses() {
		final Collection<? extends Number> collection = list(INTEGER_ONE, DOUBLE_TWO);
		final List<? extends Number> expected = list(INTEGER_ONE, DOUBLE_TWO);

		assertEquals(expected, asList(new Number[] { INTEGER_ONE, DOUBLE_TWO }));
		assertEquals(expected, asList(collection));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAsMapReturnsAMapOfItemsKeyedOnTheGivenKeysWithValuesOfTheGivenValues() {
		final List<Object> keys = list(new Object(), new Object());
		final List<Object> values = list(new Object(), new Object());

		final Map expectedOut = new HashMap();
		expectedOut.put(keys.get(0), values.get(0));
		expectedOut.put(keys.get(1), values.get(1));

		assertEquals(expectedOut, asMap(keys, values));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAsMapWithFunctorReturnsAMapOfItemsKeyedOnTheItemMappedByTheGivenFunctor() {
		final List<Object> in = list(new Object(), new Object());
		final List<Object> mappedIn = list(new Object(), new Object());

		final Map expectedOut = new HashMap();
		expectedOut.put(mappedIn.get(0), in.get(0));
		expectedOut.put(mappedIn.get(1), in.get(1));

		assertEquals(expectedOut, asMap(in, setUpFunctorExpectations(in, mappedIn)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAsMapWithFunctorReturnsAnEmptyMapWhenTheGivenCollectionIsEmpty() {
		final Map map = asMap(list(), functor);
		assertTrue(map.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAsSet() {
		final Set set = asSet(list(1, "Foo"));
		assertEquals(2, set.size());
		assertTrue(set.contains(1));
		assertTrue(set.contains("Foo"));
	}

	@Test
	public void testListReturnsAListContainingAllGivenItems() {
		final String item1 = "item1";
		final String item2 = "item2";

		final List<String> list = list(item1, item2);

		assertEquals(2, list.size());
		assertEquals(item1, list.get(0));
		assertEquals(item2, list.get(1));
	}

	@Test
	public void testListReturnsAnEmptyListWhenNoItemsAreGiven() {
		final List<Object> list = list();
		assertTrue(list.isEmpty());
	}

	@Test
	public void testListReturnsModifiableList() {
		// this will blow up with unsupported operation if its not
		list(FOO, BAR).add("baz");
	}

	@Test
	public void testMakeList() throws Exception {
		final List<Character> result = makeList(4, 'c');
		assertEquals(4, result.size());
		assertEquals(list('c', 'c', 'c', 'c'), result);
	}

	@Test
	public void testSetReturnsAnEmptySetWhenNoItemsGiven() {
		assertTrue(set().isEmpty());
	}

	@Test
	public void testSetReturnsSetContainingExpectedItems() {
		final Set<String> set = set(FOO, BAR, BAR);

		assertEquals(2, set.size());
		assertTrue(set.contains(FOO));
		assertTrue(set.contains(BAR));
	}

	@Test
	public void testToArrayWithList() {
		final String[] result = asArray(list(FOO, BAR));

		assertEquals(2, result.length);
		assertEquals(FOO, result[0]);
		assertEquals(BAR, result[1]);
	}

	@Test
	public void testToArrayWithSet() {
		final String[] result = Coercions.asArray(set(FOO, BAR));

		assertEquals(2, result.length);
		assertEquals(FOO, result[0]);
		assertEquals(BAR, result[1]);
	}

	@Test
	public void testCast() throws Exception {
		List<CharSequence> expected = list((CharSequence) "a");
		assertEquals(expected, cast(CharSequence.class, list("a")));
	}

	@SuppressWarnings("unchecked")
	private void verifyAsFunctorWithValidArgument(final int key, final Integer returnValue, final boolean containsKey,
			final boolean allowUncontainedKeys) {
		final Mock mockMap = mock(Map.class);
		mockMap.stubs().method("containsKey").with(eq(key)).will(returnValue(containsKey));
		mockMap.expects(once()).method("get").with(eq(key)).will(returnValue(returnValue));

		assertEquals(returnValue, asFunctor((Map) mockMap.proxy(), allowUncontainedKeys).execute(key));
	}

	/**
	 * @see http://jira.codehaus.org/browse/JEDI-22
	 */
	@Test
	public void testAsArrayWithSubclassesButTypeSpecified() throws Exception {
		asArray(FooSuper.class, list(new FooSub1(), new FooSub2()));
	}

	class FooSuper {}
	class FooSub1 extends FooSuper {}
	class FooSub2 extends FooSuper {}
}
