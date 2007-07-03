package jedi.functional;

import static jedi.assertion.Assert.*;
import jedi.filters.NotNullFilter;

import java.lang.reflect.Array;
import java.util.*;

public class Coercions {

	/**
	 * Create an array from the given parameter list
	 */
	public static <T> T[] array(T... items) {
	    assertNotNull(items, "items");
	    return items;
	}

	/**
	 * Produce an array from a collection. All of the <code>items</code> must be of the same class, otherwise an
	 * {@link ArrayStoreException} will be thrown.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(Collection<T> items) {
	    assertNotNull(items, "items");
	    return items.toArray((T[]) Array.newInstance(FunctionalPrimitives.head(FunctionalPrimitives.select(items, new NotNullFilter<T>())).getClass(), items.size()));
	}

	/**
	 * Make a list from the given parameters
	 */
	public static <T> List<T> list(T... items) {
	    final T[] items1 = items;
	    return asList(items1);
	}

	/**
	 * Copy the elements in the given array to a new list
	 */
	public static <T> List<T> asList(T[] items) {
	    assertNotNull(items, "items");
	    return new ArrayList<T>(Arrays.asList(items));
	}

	/**
	 * Copy the given collection into a new list
	 */
	public static <T> List<T> asList(Collection<T> items) {
	    assertNotNull(items, "items");
	    return new ArrayList<T>(items);
	}

	/**
	 * Make a set from the given items
	 */
	public static <T> Set<T> set(T... items) {
	    return asSet(items);
	}

	/**
	 * Copy the elements in the given array to a new set
	 */
	public static <T> Set<T> asSet(T[] items) {
	    return new HashSet<T>(asList(items));
	}

	/**
	 * Copy the given collection into a new set
	 */
	public static <T> Set<T> asSet(Collection<T> items) {
	    assertNotNull(items, "items");
	    return new HashSet<T>(items);
	}

	/**
	 * Create a {@link jedi.functional.Functor Functor} which uses the given <code>map</code> as the transform between
	 * argument (key) and result (value). The returned functor can optionally deal with an argument which does not exist
	 * as a key in the <code>map</code> by (a) throwing an {@link jedi.assertion.AssertionError AssertionError} or (b)
	 * returning <code>null</code>.
	 */
	public static <T, R> Functor<T, R> asFunctor(final Map<T, R> map, final boolean allowUncontainedKeys) {
	    assertNotNull(map, "map");
	
	    return new Functor<T, R>() {
	        public R execute(T key) {
	            assertTrue(allowUncontainedKeys || map.containsKey(key), "Unknown key", key);
	            return map.get(key);
	        }
	    };
	}

	/**
	 * Create a {@link jedi.functional.Filter Filter} which uses the given <code>functor</code> to map from one domain
	 * (T) to another (R) and then applies the given <code>filter</code> to R. <i>i.e.</i> the result of <code>execut</code>ing
	 * the returned filter with a value <code>t</code> is equivalent to <code>filter.execute(functor.execute(t))</code>.
	 */
	public static <T, R> Filter<T> asFilter(final Functor<T, R> functor, final Filter<? super R> filter) {
	    assertNotNull(functor, "functor");
	    assertNotNull(filter, "filter");
	
	    return new Filter<T>() {
	        public Boolean execute(T t) {
	            return filter.execute(functor.execute(t));
	        }
	    };
	}

	/**
	 * Create a map whose values are the given <code>items</code> each of which is keyed on the result of applying the
	 * given <code>keyFunctor</code> to the item.
	 */
	public static <K, T> Map<K, T> asMap(Collection<T> items, Functor<? super T, K> keyFunctor) {
	    assertNotNull(items, "items");
	    assertNotNull(keyFunctor, "keyFunctor");
	
	    Map<K, T> map = new HashMap<K, T>();
	
	    for (T item : items) {
	        map.put(keyFunctor.execute(item), item);
	    }
	
	    assertEqual(items.size(), map.size(), "same size");
	
	    return map;
	}

	/**
	 * Produce a collection with a different instantiated generic type from a given collection.
	 */
	public static <T, R> List<R> cast(Class<R> returnType, Collection<T> items) {
	    assertNotNull(returnType, "returnType");
	
	    return FunctionalPrimitives.collect(items, new Functor<T, R>() {
	        @SuppressWarnings("unchecked")
	        public R execute(T value) {
	            return (R) value;
	        }
	    });
	}

    protected Coercions() {
    }
}
