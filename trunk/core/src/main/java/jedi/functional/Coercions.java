package jedi.functional;

import static jedi.assertion.Assert.assertEqual;
import static jedi.assertion.Assert.assertNotNull;
import static jedi.assertion.Assert.assertTrue;
import static jedi.functional.FunctionalPrimitives.head;
import static jedi.functional.FunctionalPrimitives.select;

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
import jedi.tuple.Tuple2;

public class Coercions {

	/**
	 * Create an array from the given parameter list
	 */
	public static <T> T[] array(final T... items) {
		assertNotNull(items, "items must not be null");
		return items;
	}

	/**
	 * Produce an array from a collection. All of the <code>items</code> must be
	 * of the same class, otherwise an {@link ArrayStoreException} will be
	 * thrown. If you want to specify the type use {@link #asArray(Class, Collection)}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(final Collection<T> items) {
		assertNotNull(items, "items must not be null");
		return items.toArray((T[]) Array.newInstance(head(select(items, new NotNullFilter<T>())).getClass(), items.size()));
	}

	/**
	 * Produce an array from a collection.
	 * @param clazz the type you want the array to be
	 * @param items to convert to array
	 * @return an array
	 */
	public static <T> T[] asArray(Class<T> clazz, Collection<T> items) {
		assertNotNull(items, "items must not be null");
		return items.toArray((T[]) Array.newInstance(clazz, items.size()));
	}

	/**
	 * Create a {@link jedi.functional.Filter Filter} which uses the given
	 * <code>functor</code> to map from one domain (T) to another (R) and then
	 * applies the given <code>filter</code> to R. <i>i.e.</i> the result of
	 * <code>executing</code> the returned filter with a value <code>t</code> is
	 * equivalent to <code>filter.execute(functor.execute(t))</code>.
	 */
	public static <T, R> Filter<T> asFilter(final Functor<T, R> functor, final Filter<? super R> filter) {
		assertNotNull(functor, "functor must not be null");
		assertNotNull(filter, "filter must not be null");

		return new Filter<T>() {
			public Boolean execute(final T t) {
				return filter.execute(functor.execute(t));
			}
		};
	}

	/**
	 * Create a {@link jedi.functional.Functor Functor} which uses the given
	 * <code>map</code> as the transform between argument (key) and result
	 * (value). The returned functor can optionally deal with an argument which
	 * does not exist as a key in the <code>map</code> by (a) throwing an
	 * {@link jedi.assertion.AssertionError AssertionError} or (b) returning
	 * <code>null</code>.
	 */
	public static <T, R> Functor<T, R> asFunctor(final Map<T, R> map, final boolean allowUncontainedKeys) {
		assertNotNull(map, "map must not be null");

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
	public static <T> List<T> asList(final Iterable<T> items) {
		assertNotNull(items, "items must not be null");
		ArrayList<T> list = new ArrayList<T>();
		for (T t : items) {
			list.add(t);
		}
		return list;
	}

	/**
	 * Copy the elements in the given array to a new list
	 */
	public static <T> List<T> asList(final T[] items) {
		assertNotNull(items, "items");
		return new ArrayList<T>(Arrays.asList(items));
	}
	
	public static <K, V> Map<K, V> asMap(final Tuple2<K, V>[] tuples) {
		assertNotNull(tuples, "tuples must not be null");
		return asMap(asList(tuples));
	}
	
	/**
	 * Make a Map from the given tuples using the {@link Tuple2#a()} values as <code>keys</code> and
	 * the {@link Tuple2#b()} values as <code>values</code>.
	 */
	public static <K, V> Map<K, V> map(final Tuple2<K, V>... tuples) {
		return asMap(tuples);
	}
	
	/**
	 * Create a Map from the given tuples using the {@link Tuple2#a()} values as <code>keys</code> and
	 * the {@link Tuple2#b()} values as <code>values</code>.
	 */
	public static <K, V> Map<K, V> asMap(final Iterable<Tuple2<K, V>> tuples) {
		assertNotNull(tuples, "tuples must not be null");

		final Map<K, V> map = new HashMap<K, V>();

		for (Tuple2<K, V> tuple : tuples) {
			map.put(tuple.a(), tuple.b());
		}

		return map;
	}

	/**
	 * Create a Map from the given <code>keys</code> and the given
	 * <code>values</code>.
	 */
	public static <K, V> Map<K, V> asMap(final Iterable<K> keys, final Iterable<V> values) {
		assertNotNull(keys, "keys must not be null");
		assertNotNull(values, "values must not be null");

		final Map<K, V> map = new HashMap<K, V>();

		final Iterator<V> valueIterator = values.iterator();
		final Iterator<K> keyIterator = keys.iterator();
		while (keyIterator.hasNext() && valueIterator.hasNext()) {
			map.put(keyIterator.next(), valueIterator.next());
		}
		
		assertTrue(!keyIterator.hasNext() && !valueIterator.hasNext(), "keys and vaues must be the same size");
		return map;
	}

	/**
	 * Create a map whose values are the given <code>items</code> each of which
	 * is keyed on the result of applying the given <code>keyFunctor</code> to
	 * the item.
	 */
	public static <K, T> Map<K, T> asMap(final Iterable<T> items, final Functor<? super T, K> keyFunctor) {
		assertNotNull(items, "items must not be null");
		assertNotNull(keyFunctor, "keyFunctor must not be null");

		final Map<K, T> map = new HashMap<K, T>();

		int count = 0;
		for (final T item : items) {
			map.put(keyFunctor.execute(item), item);
			count++;
		}

		assertEqual(count, map.size(), "items and map should be the same size but are not");

		return map;
	}

	/**
	 * Copy the given collection into a new set
	 */
	public static <T> Set<T> asSet(final Collection<T> items) {
		assertNotNull(items, "items must not be null");
		return new HashSet<T>(items);
	}

	/**
	 * Copy the elements in the given array to a new set
	 */
	public static <T> Set<T> asSet(final T[] items) {
		return new HashSet<T>(asList(items));
	}

	/**
	 * Produce a collection with a different instantiated generic type from a
	 * given collection.
	 */
	public static <T, R> List<R> cast(final Class<R> returnType, final Iterable<T> items) {
		assertNotNull(returnType, "returnType must not be null");

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
		return asList(items);
	}

	/**
	 * Make a set from the given items
	 */
	public static <T> Set<T> set(final T... items) {
		return asSet(items);
	}

	private Coercions() {
	}
}
