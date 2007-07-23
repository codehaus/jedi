package jedi.functional;

import static jedi.assertion.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jedi.filters.NotNullFilter;

public class Coercions {

    /**
     * Create an array from the given parameter list
     */
    public static <T> T[] array(final T... items) {
        assertNotNull(items, "items");
        return items;
    }

    /**
     * Produce an array from a collection. All of the <code>items</code> must be of the same class, otherwise an {@link ArrayStoreException} will be thrown.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] asArray(final Collection<T> items) {
        assertNotNull(items, "items");
        return items.toArray((T[]) Array.newInstance(FunctionalPrimitives.head(FunctionalPrimitives.select(items, new NotNullFilter<T>())).getClass(), items.size()));
    }

    /**
     * Create a {@link jedi.functional.Filter Filter} which uses the given <code>functor</code> to map from one domain (T) to another (R) and then applies the given
     * <code>filter</code> to R. <i>i.e.</i> the result of <code>execut</code>ing the returned filter with a value <code>t</code> is equivalent to
     * <code>filter.execute(functor.execute(t))</code>.
     */
    public static <T, R> Filter<T> asFilter(final Functor<T, R> functor, final Filter< ? super R> filter) {
        assertNotNull(functor, "functor");
        assertNotNull(filter, "filter");

        return new Filter<T>() {
            public Boolean execute(final T t) {
                return filter.execute(functor.execute(t));
            }
        };
    }

    /**
     * Create a {@link jedi.functional.Functor Functor} which uses the given <code>map</code> as the transform between argument (key) and result (value). The returned functor can
     * optionally deal with an argument which does not exist as a key in the <code>map</code> by (a) throwing an {@link jedi.assertion.AssertionError AssertionError} or (b)
     * returning <code>null</code>.
     */
    public static <T, R> Functor<T, R> asFunctor(final Map<T, R> map, final boolean allowUncontainedKeys) {
        assertNotNull(map, "map");

        return new Functor<T, R>() {
            public R execute(final T key) {
                assertTrue(allowUncontainedKeys || map.containsKey(key), "Unknown key", key);
                return map.get(key);
            }
        };
    }

    /**
     * Copy the given collection into a new list
     */
    public static <T> List<T> asList(final Collection<T> items) {
        assertNotNull(items, "items");
        return new ArrayList<T>(items);
    }

    /**
     * Copy the elements in the given array to a new list
     */
    public static <T> List<T> asList(final T[] items) {
        assertNotNull(items, "items");
        return new ArrayList<T>(Arrays.asList(items));
    }

    /**
     * Create a Map from the given <code>keys</code> and the given <code>values</code>.
     */
    public static <K, V> Map<K, V> asMap(final Collection<K> keys, final Collection<V> values) {
        assertNotNull(keys, "keys");
        assertNotNull(values, "values");
        assertEqual(keys.size(), values.size(), "keys.size == values.size");

        final Map<K, V> map = new HashMap<K, V>();

        final Iterator<V> valueIterator = values.iterator();
        for (final K key : keys) {
            map.put(key, valueIterator.next());
        }

        return map;
    }

    /**
     * Create a map whose values are the given <code>items</code> each of which is keyed on the result of applying the given <code>keyFunctor</code> to the item.
     */
    public static <K, T> Map<K, T> asMap(final Collection<T> items, final Functor< ? super T, K> keyFunctor) {
        assertNotNull(items, "items");
        assertNotNull(keyFunctor, "keyFunctor");

        final Map<K, T> map = new HashMap<K, T>();

        for (final T item : items) {
            map.put(keyFunctor.execute(item), item);
        }

        assertEqual(items.size(), map.size(), "same size");

        return map;
    }

    /**
     * Copy the given collection into a new set
     */
    public static <T> Set<T> asSet(final Collection<T> items) {
        assertNotNull(items, "items");
        return new HashSet<T>(items);
    }

    /**
     * Copy the elements in the given array to a new set
     */
    public static <T> Set<T> asSet(final T[] items) {
        return new HashSet<T>(asList(items));
    }

    /**
     * Produce a collection with a different instantiated generic type from a given collection.
     */
    public static <T, R> List<R> cast(final Class<R> returnType, final Collection<T> items) {
        assertNotNull(returnType, "returnType");

        return FunctionalPrimitives.collect(items, new Functor<T, R>() {
            @SuppressWarnings("unchecked")
            public R execute(final T value) {
                return (R) value;
            }
        });
    }

    /**
     * Make a list from the given parameters
     */
    public static <T> List<T> list(final T... items) {
        final T[] items1 = items;
        return asList(items1);
    }

    /**
     * Make a set from the given items
     */
    public static <T> Set<T> set(final T... items) {
        return asSet(items);
    }

    protected Coercions() {
    }
}
