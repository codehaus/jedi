package jedi.functional;

import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jmock.Mock;

public class CoercionsTest extends ClosureTestCase {
    @SuppressWarnings("unchecked")
    public void testArrayWithMultipleClasses() {
        final Number[] a = Coercions.array(INTEGER_ONE, DOUBLE_TWO);

        assertSame(INTEGER_ONE, a[0]);
        assertSame(DOUBLE_TWO, a[1]);
    }

    public void testArrayWithSingleClass() {
        final String[] a = Coercions.array(FOO, BAR);
        assertSame(FOO, a[0]);
        assertSame(BAR, a[1]);
    }

    @SuppressWarnings("unchecked")
    public void testAsArray() {
        final List<String> list = Coercions.list(FOO, BAR);
        final String[] a = Coercions.asArray(list);
        assertSame(FOO, a[0]);
        assertSame(BAR, a[1]);
    }

    public void testAsFunctorReturnsNullWhenKeyIsNotInMapAndAllowUncontainedKeysIsTrue() {
        verifyAsFunctorWithValidArgument(new Random().nextInt(), null, true, true);
    }

    public void testAsFunctorReturnsValueFromMapWhenAllowUncontainedValuesIsFalse() {
        verifyAsFunctorWithValidArgument(new Random().nextInt(), null, true, false);
    }

    public void testAsFunctorReturnsValueFromMapWhenAllowUncontainedValuesIsTrue() {
        final Random random = new Random();
        verifyAsFunctorWithValidArgument(random.nextInt(), random.nextInt(), true, true);
    }

    @SuppressWarnings("unchecked")
    public void testAsFunctorThrowsAnAssertionErrorWhenKeyIsNotInMapAndAllowUncontainedKeysIsFalse() {
        final int key = new Random().nextInt();
        final Mock mockMap = mock(Map.class);
        mockMap.stubs().method("containsKey").with(eq(key)).will(returnValue(false));

        final Functor Functor = Coercions.asFunctor((Map) mockMap.proxy(), false);
        try {
            Functor.execute(key);
            fail();
        } catch (final jedi.assertion.AssertionError error) {
            // Expected
        }
    }

    public void testAsList() {
        final Collection<String> collection = Coercions.list(FOO, BAR);
        final List<String> expected = Coercions.list(FOO, BAR);

        assertEquals(expected, Coercions.asList(new String[]{FOO, BAR}));
        assertEquals(expected, Coercions.asList(collection));
    }

    @SuppressWarnings("unchecked")
    public void testAsListWithDifferentClasses() {
        final Collection< ? extends Number> collection = Coercions.list(INTEGER_ONE, DOUBLE_TWO);
        final List< ? extends Number> expected = Coercions.list(INTEGER_ONE, DOUBLE_TWO);

        assertEquals(expected, Coercions.asList(new Number[]{INTEGER_ONE, DOUBLE_TWO}));
        assertEquals(expected, Coercions.asList(collection));
    }

    public void testAsMapReturnsAMapOfItemsKeyedOnTheGivenKeysWithValuesOfTheGivenValues() {
        final List<Object> keys = Coercions.list(new Object(), new Object());
        final List<Object> values = Coercions.list(new Object(), new Object());

        final Map expectedOut = new HashMap();
        expectedOut.put(keys.get(0), values.get(0));
        expectedOut.put(keys.get(1), values.get(1));

        assertEquals(expectedOut, Coercions.asMap(keys, values));
    }

    @SuppressWarnings("unchecked")
    public void testAsMapWithFunctorReturnsAMapOfItemsKeyedOnTheItemMappedByTheGivenFunctor() {
        final List<Object> in = Coercions.list(new Object(), new Object());
        final List<Object> mappedIn = Coercions.list(new Object(), new Object());

        final Map expectedOut = new HashMap();
        expectedOut.put(mappedIn.get(0), in.get(0));
        expectedOut.put(mappedIn.get(1), in.get(1));

        assertEquals(expectedOut, Coercions.asMap(in, setUpFunctorExpectations(in, mappedIn)));
    }

    @SuppressWarnings("unchecked")
    public void testAsMapWithFunctorReturnsAnEmptyMapWhenTheGivenCollectionIsEmpty() {
        final Map map = Coercions.asMap(Coercions.list(), functor);
        assertTrue(map.isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testAsSet() {
        final Set set = Coercions.asSet(Coercions.list(1, "Foo"));
        assertEquals(2, set.size());
        assertTrue(set.contains(1));
        assertTrue(set.contains("Foo"));
    }

    public void testListReturnsAListContainingAllGivenItems() {
        final String item1 = "item1";
        final String item2 = "item2";

        final List<String> list = Coercions.list(item1, item2);

        assertEquals(2, list.size());
        assertEquals(item1, list.get(0));
        assertEquals(item2, list.get(1));
    }

    @SuppressWarnings("unchecked")
    public void testListReturnsAnEmptyListWhenNoItemsAreGiven() {
        final List<Object> list = Coercions.list();
        assertTrue(list.isEmpty());
    }

    public void testListReturnsModifiableList() {
        // this will blow up with unsupported opperation if its not
        Coercions.list(FOO, BAR).add("baz");
    }

    public void testMakeList() throws Exception {
        final List<Character> result = makeList(4, 'c');
        assertEquals(4, result.size());
        assertEquals(Coercions.list('c', 'c', 'c', 'c'), result);
    }

    public void testSetReturnsAnEmptySetWhenNoItemsGiven() {
        assertTrue(Coercions.set().isEmpty());
    }

    public void testSetReturnsSetContainingExpectedItems() {
        final Set<String> set = Coercions.set(FOO, BAR, BAR);

        assertEquals(2, set.size());
        assertTrue(set.contains(FOO));
        assertTrue(set.contains(BAR));
    }

    public void testToArrayWithList() {
        final String[] result = Coercions.asArray(Coercions.list(FOO, BAR));

        assertEquals(2, result.length);
        assertEquals(FOO, result[0]);
        assertEquals(BAR, result[1]);
    }

    public void testToArrayWithSet() {
        final String[] result = Coercions.asArray(Coercions.set(FOO, BAR));

        assertEquals(2, result.length);
        assertEquals(FOO, result[0]);
        assertEquals(BAR, result[1]);
    }

    @SuppressWarnings("unchecked")
    private void verifyAsFunctorWithValidArgument(final int key, final Integer returnValue, final boolean containsKey, final boolean allowUncontainedKeys) {
        final Mock mockMap = mock(Map.class);
        mockMap.stubs().method("containsKey").with(eq(key)).will(returnValue(containsKey));
        mockMap.expects(once()).method("get").with(eq(key)).will(returnValue(returnValue));

        assertEquals(returnValue, Coercions.asFunctor((Map) mockMap.proxy(), allowUncontainedKeys).execute(key));
    }
}
