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
        Number[] a = Coercions.array(INTEGER_ONE, DOUBLE_TWO);
        
        assertSame(INTEGER_ONE, a[0]);
        assertSame(DOUBLE_TWO, a[1]);
    }

    public void testArrayWithSingleClass() {
        String[] a = Coercions.array(FOO, BAR);
        assertSame(FOO, a[0]);
        assertSame(BAR, a[1]);
    }

    @SuppressWarnings("unchecked")
    public void testAsArray() {
        List<String> list = Coercions.list(FOO, BAR);
        String[] a = Coercions.asArray(list);
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
    	int key = new Random().nextInt();
    	Mock mockMap = mock(Map.class);
    	mockMap.stubs().method("containsKey").with(eq(key)).will(returnValue(false));
    
    	final Functor Functor = Coercions.asFunctor((Map) mockMap.proxy(), false);
    	try {
    		Functor.execute(key);
    		fail();
    	} catch (jedi.assertion.AssertionError error) {
    		// Expected
    	}
    }

    public void testAsList() {
    	Collection<String> collection = Coercions.list(FOO, BAR);
    	List<String> expected = Coercions.list(FOO, BAR);
    
    	assertEquals(expected, Coercions.asList(new String[] { FOO, BAR }));
    	assertEquals(expected, Coercions.asList(collection));
    }

    @SuppressWarnings("unchecked")
    public void testAsListWithDifferentClasses() {
        Collection<? extends Number> collection = Coercions.list(INTEGER_ONE, DOUBLE_TWO);
        List<? extends Number> expected = Coercions.list(INTEGER_ONE, DOUBLE_TWO);
    
        assertEquals(expected, Coercions.asList(new Number[] { INTEGER_ONE, DOUBLE_TWO }));
        assertEquals(expected, Coercions.asList(collection));
    }

    @SuppressWarnings("unchecked")
    public void testAsMapReturnsAMapOfItemsKeyedOnTheItemMappedByTheGivenFunctor() {
    	List<Object> in = Coercions.list(new Object(), new Object());
    	List<Object> mappedIn = Coercions.list(new Object(), new Object());
    
    	Map expectedOut = new HashMap();
    	expectedOut.put(mappedIn.get(0), in.get(0));
    	expectedOut.put(mappedIn.get(1), in.get(1));
    
    	assertEquals(expectedOut, Coercions.asMap(in, setUpFunctorExpectations(in, mappedIn)));
    }

    @SuppressWarnings("unchecked")
    public void testAsMapReturnsAnEmptyMapWhenTheGivenCollectionIsEmpty() {
    	Map map = Coercions.asMap(Coercions.list(), functor);
    	assertTrue(map.isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testAsSet() {
    	Set set = Coercions.asSet(Coercions.list(1, "Foo"));
    	assertEquals(2, set.size());
    	assertTrue(set.contains(1));
    	assertTrue(set.contains("Foo"));
    }

    public void testListReturnsAListContainingAllGivenItems() {
    	String item1 = "item1";
    	String item2 = "item2";
    
    	List<String> list = Coercions.list(item1, item2);
    
    	assertEquals(2, list.size());
    	assertEquals(item1, list.get(0));
    	assertEquals(item2, list.get(1));
    }

    @SuppressWarnings("unchecked")
    public void testListReturnsAnEmptyListWhenNoItemsAreGiven() {
    	List<Object> list = Coercions.list();
    	assertTrue(list.isEmpty());
    }

    public void testListReturnsModifiableList() {
    	// this will blow up with unsupported opperation if its not
    	Coercions.list(FOO, BAR).add("baz");
    }

    public void testMakeList() throws Exception {
    	List<Character> result = makeList(4, 'c');
    	assertEquals(4, result.size());
    	assertEquals(Coercions.list('c', 'c', 'c', 'c'), result);
    }

    public void testSetReturnsAnEmptySetWhenNoItemsGiven() {
    	assertTrue(Coercions.set().isEmpty());
    }

    public void testSetReturnsSetContainingExpectedItems() {
    	Set<String> set = Coercions.set(FOO, BAR, BAR);
    
    	assertEquals(2, set.size());
    	assertTrue(set.contains(FOO));
    	assertTrue(set.contains(BAR));
    }

    public void testToArrayWithList() {
    	String[] result = Coercions.asArray(Coercions.list(FOO, BAR));
    
    	assertEquals(2, result.length);
    	assertEquals(FOO, result[0]);
    	assertEquals(BAR, result[1]);
    }

    public void testToArrayWithSet() {
    	String[] result = Coercions.asArray(Coercions.set(FOO, BAR));
    
    	assertEquals(2, result.length);
    	assertEquals(FOO, result[0]);
    	assertEquals(BAR, result[1]);
    }

    @SuppressWarnings("unchecked")
    private void verifyAsFunctorWithValidArgument(int key, Integer returnValue, boolean containsKey, boolean allowUncontainedKeys) {
    	Mock mockMap = mock(Map.class);
    	mockMap.stubs().method("containsKey").with(eq(key)).will(returnValue(containsKey));
    	mockMap.expects(once()).method("get").with(eq(key)).will(returnValue(returnValue));
    
    	assertEquals(returnValue, Coercions.asFunctor((Map) mockMap.proxy(), allowUncontainedKeys).execute(key));
    }

}
