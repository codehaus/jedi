package jedi.functional;

import static jedi.assertion.Assert.assertEqual;
import static jedi.assertion.Assert.assertGreaterThanOrEqualTo;
import static jedi.assertion.Assert.assertLessThanOrEqualTo;
import static jedi.assertion.Assert.assertNotNull;
import static jedi.assertion.Assert.assertNotNullOrEmpty;
import static jedi.assertion.Assert.assertTrue;
import static jedi.functional.Coercions.asList;
import static jedi.functional.Coercions.list;
import static jedi.functional.Coercions.set;
import static jedi.functional.Comparables.sort;
import static jedi.functional.Comparables.sortInPlace;
import static jedi.functional.FirstOrderLogic.invert;
import static jedi.option.Options.option;
import static jedi.option.Options.some;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jedi.assertion.AssertionError;
import jedi.option.None;
import jedi.option.Option;
import jedi.option.Options;
import jedi.option.Some;

/**
 * I provide operations of the kind found in Functional Programming languages.
 * This allows you to remove a great deal of clutter from production code.
 * Ideally, you will never need to write another 'for' loop again, and a great
 * deal of explicit conditional logic should be removable as well.
 * <p/>
 * Generally, functions that transform collections (using functors or whatever)
 * preserve the iteration order of the given collection in the result.
 */
public class FunctionalPrimitives {

	private FunctionalPrimitives() {
	}

	@SuppressWarnings("unchecked")
	private static final Comparator<Collection> COLLECTION_SIZE = new Comparator<Collection>() {
		public int compare(Collection o1, Collection o2) {
			return o1.size() - o2.size();
		}
	};

	private static <K, V> void addToGroup(final K key, final V value, final Map<K, List<V>> groups) {
		List<V> group = groups.get(key);
		if (group == null) {
			groups.put(key, group = new ArrayList<V>());
		}
		group.add(value);
	}

	private static <T> Collection<T> toCollection(Iterable<T> iterable) {
		return iterable instanceof Collection ? (Collection<T>) iterable : asList(iterable);
	}

	private static <T> List<T> toList(Iterable<T> iterable) {
		return iterable instanceof List ? (List<T>) iterable : asList(iterable);
	}

	/**
	 * Append all of the elements in all of the given <code>collections</code>
	 * into one list. All of the elements of the first item in
	 * <code>collections</code> are appended first, then the items in the second
	 * collection, etc.
	 * 
	 * @see #append(Collection...)
	 */
	public static <T> List<T> append(final Iterable<? extends Iterable<? extends T>> collections) {
		return flatten(collections, new Functor<Iterable<? extends T>, Iterable<T>>() {
			@SuppressWarnings("unchecked")
			public Iterable<T> execute(final Iterable<? extends T> value) {
				return (Iterable<T>) value;
			}
		});
	}

	/**
	 * Append all of the elements in all of the given <code>collections</code>
	 * into one list. All of the elements of the first item in
	 * <code>collections</code> are appended first, then the items in the second
	 * collection, etc. Equivalent to <code>append(list(collections))</code>
	 * 
	 * @see #append(Iterable)
	 */
	public static <T> List<T> append(final Collection<? extends T>... collections) {
		return append(list(collections));
	}

	/**
	 * Apply <code>functor</code> to each element of <code>items</code> and
	 * return the list of results.
	 * 
	 * @see #collect(Object[],Functor)
	 */
	public static <T, R> List<R> collect(final Iterable<T> items, final Functor<? super T, R> functor) {
		assertNotNull(items, "items must not be null");
		assertNotNull(functor, "functor must not be null");

		final List<R> mapped = new ArrayList<R>();

		for (final T item : items) {
			mapped.add(functor.execute(item));
		}

		return mapped;
	}

	/**
	 * Apply <code>functor</code> to each element of <code>items</code> and
	 * return the list of results. The iteration order of <code>items</code> is
	 * preserved in the returned list.
	 * <p/>
	 * Equivalent to <code>collect(functor, asList(items))</code>
	 * 
	 * @see #collect(Iterable, Functor)
	 */
	public static <T, R> List<R> collect(final T[] items, final Functor<? super T, R> functor) {
		return collect(asList(items), functor);
	}

	/**
	 * Get all but the first n elements of
	 * <code>items<code>. See <a href="http://srfi.schemers.org/srfi-1/srfi-1.html#drop">SRFI-1</a>
	 */
	public static <T> List<T> drop(final int n, final Iterable<T> items) {
		assertNotNull(items, "items must not be null");
		ArrayList<T> list = new ArrayList<T>();
		int i = 0;
		for (T t : items) {
			if (i >= n) {
				list.add(t);
			}
			i++;
		}
		return list;
	}

	/**
	 * Get all but the last n elements of <code>items</code>. See <a
	 * href="http://srfi.schemers.org/srfi-1/srfi-1.html#drop-right">SRFI-1</a>
	 */
	public static <T> List<T> dropRight(final int n, final Iterable<T> items) {
		assertNotNull(items, "items must not be null");
		Collection<T> collection = toCollection(items);
		assertLessThanOrEqualTo(collection.size(), n, "n should be less than or equal to items.size but is not");
		return take(collection.size() - n, collection);
	}

	/**
	 * Suppose there is a collection of items (c1, c2, c3), each of which
	 * contains a collection <i>i.e.</i> (c1 = (c1_1, c1_2, ...), c2=(c2_1,
	 * c2_2, ...). I can produce a collection containing all of the 'leaf' items
	 * <i>i.e.</i>(c1_1, c1_2, ..., c2_1, c2_2)
	 * 
	 * @param items
	 *            The collection of items containing the collection of leaves
	 * @param functor
	 *            Given an element of the 'top' collection, this can obtain the
	 *            collection of 'leaf' objects to accumulate
	 */
	public static <T, R> List<R> flatten(final Iterable<T> items, final Functor<? super T, ? extends Iterable<? extends R>> functor) {
		assertNotNull(items, "items must not be null");
		assertNotNull(functor, "functor must not be null");

		final List<R> list = new ArrayList<R>();

		for (final T t : items) {
			addAll(list, functor.execute(t));
		}

		return list;
	}

	/**
	 * Add all the elements of an iterable to a collection.
	 */
	public static <T> Collection<T> addAll(Collection<T> list, Iterable<? extends T> iterable) {
		assertNotNull(list, "list must not be null");
		assertNotNull(iterable, "iterable must not be null");
		for (T object : iterable) {
			list.add(object);
		}
		return list;
	}

	/**
	 * Fold passes each item of a collection with an accumulated value to a
	 * functor.
	 * <p/>
	 * For example, to sum the elements of a list:
	 * <p/>
	 * 
	 * <pre>
	 *           Functor2&lt;Integer, Integer&gt; summer = new Functor2&lt;Integer, Integer&gt;() {
	 *           	public Integer execute(Integer accumulator, Integer value) {
	 *           		return accumulator + value;
	 *           	}
	 *           };
	 *           fold(10, summer, list(1, 2, 3, 4)) will return 20 (initial value of 10 + the sum of 1 .. 4)
	 * </pre>
	 * 
	 * <p/>
	 * For a more comprehensive description, see <a
	 * href="http://srfi.schemers.org/srfi-1/srfi-1.html#FoldUnfoldMap"
	 * >SRFI-1</a>
	 */
	public static <T, R, I extends R> R fold(final I initialValue, final Iterable<T> collection, final Functor2<R, ? super T, R> functor2) {
		assertNotNull(collection, "collection must not be null");
		assertNotNull(functor2, "functor2 must not be null");

		R accumulated = initialValue;
		for (final T t : collection) {
			accumulated = functor2.execute(accumulated, t);
		}
		return accumulated;
	}

	/**
	 * Iterate over a collection of <code>items</code> applying the given
	 * <code>command</code> to each
	 */
	public static <T> Iterable<T> forEach(final Iterable<T> items, final Command<? super T> command) {
		assertNotNull(command, "command must not be null");
		assertNotNull(items, "items must not be null");

		for (final T item : items) {
			command.execute(item);
		}

		return items;
	}

	/**
	 * A synonym for {@link #partition(Iterable, Functor)} Group the elements of
	 * a collection such that all elements that are mapped to the same value by
	 * a given <code>keyFunctor</code> are in the same group. The groups are
	 * returned as a map in which each value is a collection of values in one
	 * group and whose key is the value returned by the <code>keyFunctor</code>
	 * for all items in the group.
	 */
	public static <K, V> Map<K, List<V>> group(final Iterable<V> toGroup, final Functor<? super V, K> keyFunctor) {
		assertNotNull(keyFunctor, "keyFunctor must not be null");
		assertNotNull(toGroup, "toGroup must not be null");

		final Map<K, List<V>> groups = new HashMap<K, List<V>>();

		for (final V v : toGroup) {
			addToGroup(keyFunctor.execute(v), v, groups);
		}

		return groups;
	}

	/**
	 * A synonym for {@link #group(Iterable, Functor)} Partition the elements of
	 * a collection such that all elements that are mapped to the same value by
	 * a given <code>keyFunctor</code> are in the same partition. The groups are
	 * returned as a map in which each value is a collection of values in one
	 * partition and whose key is the value returned by the
	 * <code>keyFunctor</code> for all items in the group.
	 */
	public static <K, V> Map<K, List<V>> partition(final Iterable<V> toPartition, final Functor<? super V, K> keyFunctor) {
		return group(toPartition, keyFunctor);
	}

	/**
	 * Get the first item (in iteration order) from a collection. The collection
	 * must contain at least one item or an
	 * {@link jedi.assertion.AssertionError AssertionError} will be thrown.
	 * 
	 * @return the first item in the collection
	 * @throws jedi.assertion.AssertionError
	 *             if the collection is empty
	 * @see #only(Collection)
	 * @see #headOrNullIfEmpty(Iterable)
	 * @see #headOrDefaultIfEmpty(Iterable, Object)
	 */
	public static <T> T head(final Iterable<T> items) {
		assertNotNull(items, "items must not be null");
		assertTrue(hasItems(items), "items must not be empty");
		return headOrNullIfEmpty(items);
	}

	/**
	 * Get the first item (in iteration order) from a collection or
	 * <code>defaultValue</code> (which may be null) if the collection is empty.
	 * 
	 * @return the first item in the collection or <code>defaultValue</code> if
	 *         the collection is empty
	 * @see #only(Collection)
	 * @see #head(Iterable)
	 * @see #headOrNullIfEmpty(Iterable)
	 */
	public static <T> T headOrDefaultIfEmpty(final Iterable<? extends T> items, final T defaultValue) {
		assertNotNull(items, "items must not be null");

		if (isEmpty(items)) {
			return defaultValue;
		}

		if (items instanceof List) {
			return ((List<? extends T>) items).get(0);
		}
		return items.iterator().next();
	}

	/**
	 * Get the first item (in iteration order) from a collection or
	 * <code>null</code> if the collection is empty.
	 * 
	 * @return the first item in the collection or null if the collection is
	 *         empty
	 * @see #only(Collection)
	 * @see #head(Iterable)
	 * @see #headOrDefaultIfEmpty(Iterable, Object)
	 */
	public static <T> T headOrNullIfEmpty(final Iterable<T> items) {
		return headOrDefaultIfEmpty(items, null);
	}

	/**
	 * Get the first item (in iteration order) from a collection as an
	 * {@link Option}.
	 * 
	 * @param items
	 * @return the first item (in iteration order) from a collection as
	 *         {@link Some} or {@link None} if the collection is empty.
	 */
	public static <T> Option<T> headOption(final Iterable<T> items) {
		return option(headOrNullIfEmpty(items));
	}

	/**
	 * Get the last item (in iteration order) from a collection. The collection
	 * must contain at least one item or an
	 * {@link jedi.assertion.AssertionError AssertionError} will be thrown.
	 * 
	 * @return the last item in the collection
	 * @throws jedi.assertion.AssertionError
	 *             if the collection is empty
	 * @see #only(Collection)
	 * @see #lastOrNullIfEmpty(Iterable)
	 * @see #lastOrDefaultIfEmpty(Iterable, Object)
	 */
	public static <T> T last(Iterable<? extends T> items) {
		assertNotNull(items, "items must not be null");
		assertTrue(hasItems(items), "items must not be empty");
		return lastOrNullIfEmpty(items);
	}

	/**
	 * Get the last item (in iteration order) from a collection or
	 * <code>defaultValue</code> (which may be null) if the collection is empty.
	 * 
	 * @return the last item in the collection or <code>defaultValue</code> if
	 *         the collection is empty
	 * @see #only(Collection)
	 * @see #last(Iterable)
	 * @see #lastOrNullIfEmpty(Iterable)
	 */
	public static <T> T lastOrDefaultIfEmpty(Iterable<? extends T> items, T defaultValue) {
		assertNotNull(items, "items must not be null");
		if (isEmpty(items)) {
			return defaultValue;
		}
		List<? extends T> l = toList(items);
		return l.get(l.size() - 1);
	}

	public static <T> T lastOrNullIfEmpty(Iterable<? extends T> items) {
		return lastOrDefaultIfEmpty(items, null);
	}

	/**
	 * Get the last item (in iteration order) from a collection as an
	 * {@link Option}.
	 * 
	 * @return the last item as a {@link Some} or {@link None} if the collection
	 *         is empty.
	 */
	public static <T> Option<T> lastOption(Collection<? extends T> items) {
		return option(lastOrNullIfEmpty(items));
	}

	/**
	 * An alias for head
	 * 
	 * @see #head(Iterable)
	 */
	public static <T> T first(Iterable<? extends T> collection) {
		return head(collection);
	}

	/**
	 * Find the first item that matches the given filter
	 * @return The first item that matches the given filter
	 * @throws AssertionError if no match can be found
	 */
	public static <T> T first(Iterable<T> all, final Filter<? super T> filter) {
		for (T t : all) {
			if(filter.execute(t)) {
				return t;
			}
		}
		throw new AssertionError("At least one item should match the filter");
	}

	/**
	 * Find the first item that matches the given filter or null if no match can be found
	 * @return The first item that matches the given filter or null if no match can be found
	 */
	public static <T> T firstOrDefault(Iterable<? extends T> all, final Filter<? super T> filter, T defaultValue) {
		for (T t : all) {
			if(filter.execute(t)) {
				return t;
			}
		}
		return defaultValue;
	}

	/**
	 * Find the first item that matches the given filter or null if no match can be found
	 * @return The first item that matches the given filter or null if no match can be found
	 */
	public static <T> T firstOrNull(Iterable<T> all, final Filter<? super T> filter) {
		return firstOrDefault(all, filter, null);
	}

	/**
	 * Find the first item that matches the given filter
	 * @return A Some Option on the first matching item or None if no items match
	 */
	public static <T> Option<T> firstOption(Iterable<T> all, final Filter<? super T> filter) {
		return Options.option(firstOrNull(all, filter));
	}

	/**
	 * Get the first item (in iteration order) from a collection as an
	 * {@link Option}.
	 * 
	 * @param items
	 * @return the first item (in iteration order) from a collection as
	 *         {@link Some} or {@link None} if the collection is empty.
	 */
	public static <T> Option<T> firstOption(final Iterable<T> items) {
		return option(headOrNullIfEmpty(items));
	}

	/**
	 * An alias for fold.
	 * 
	 * @see #fold(Object, Iterable, Functor2)
	 */
	public static <T, R, I extends R> R inject(final I initialValue, final Iterable<T> collection, final Functor2<R, ? super T, R> functor2) {
		return fold(initialValue, collection, functor2);
	}

	/**
	 * Join, with default delimiter (empty string)
	 * 
	 * @see #join(Object[], String)
	 */
	public static String join(final Iterable<?> items) {
		return join(items, "");
	}

	public static String join(final Iterable<?> items, final String delimiter) {
		assertNotNull(items, "items must not be null");
		assertNotNull(delimiter, "delimiter must not be null");

		Iterator<?> iterator = items.iterator();
		final StringBuffer sb = new StringBuffer();

		while (iterator.hasNext()) {
			sb.append(iterator.next());
			if (iterator.hasNext()) {
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}

	/**
	 * Returns a string created by converting each element of an array to a
	 * string, separated by delimiter. Emulates Array.join in Ruby.
	 */
	public static String join(final Object[] items, final String delimiter) {
		assertNotNull(items, "items must not be null");
		return join(asList(items), delimiter);
	}

	/**
	 * Returns an n-element list. Element i of the list, where 0 <= i < n, is
	 * produced by the functor. For a more comprehensive description, see <a
	 * href
	 * ="http://srfi.schemers.org/srfi-1/srfi-1.html#list-tabulate">SRFI-1</a>
	 * 
	 * @param n
	 *            the length of the list
	 * @param functor
	 *            the functor taking an integer and returning an <R>
	 */
	public static <R> List<R> listTabulate(final int n, final Functor<Integer, R> functor) {
		assertNotNull(functor, "functor must not be null");
		final List<R> list = new ArrayList<R>(n);
		for (int i = 0; i < n; i++) {
			list.add(functor.execute(i));
		}
		return list;
	}

	/**
	 * Find the biggest collection in a collection of collections
	 * 
	 * @param collections
	 * @return the shortest list
	 */
	public static <U, T extends Collection<? extends U>> T longest(final Iterable<T> collections) {
		assertNotNull(collections, "collections must not be null");
		assertTrue(hasItems(collections), "collections must have at least one item");
		return head(reverse(sortInPlace(asList(collections), COLLECTION_SIZE)));
	}

	/**
	 * Returns an n-element list, whose elements are all the value
	 * <code>fill</code>. For a more comprehensive description, see <a
	 * href="http://srfi.schemers.org/srfi-1/srfi-1.html#make-list">SRFI-1</a>
	 */
	public static <T> List<T> makeList(final int n, final T fill) {
		assertNotNull(fill, "fill must not be null");
		final List<T> list = new ArrayList<T>(n);
		for (int i = 0; i < n; i++) {
			list.add(fill);
		}
		return list;
	}

	/**
	 * Return the one and only item in the given collection.
	 * 
	 * @return the item in the collection
	 * @throws jedi.assertion.AssertionError
	 *             if the collection contains less or more than one item
	 * @see #head(Iterable)
	 * @see #headOrNullIfEmpty(Iterable)
	 */
	public static <T> T only(final Collection<T> items) {
		assertNotNull(items, "items must not be null");
		assertEqual(1, items.size(), "items must contain only one element");
		return head(items);
	}

	/**
	 * Create the Cartesian product of two collections, using a {@link Functor2
	 * functor} as a factory of objects to represent the pair-wise products.
	 */
	public static <T, U, R> List<R> produce(final Iterable<T> left, final Iterable<U> right, final Functor2<? super T, ? super U, R> factory) {
		final List<R> product = new ArrayList<R>();

		for (final T t : left) {
			for (final U u : right) {
				product.add(factory.execute(t, u));
			}
		}

		return product;
	}

	/**
	 * Filter a collection of <code>items</code>, returning only those that do
	 * not match a given <code>filter</code>, this is the inverse of select.
	 * 
	 * @see #select(Iterable, Filter)
	 */
	public static <T> List<T> reject(final Iterable<T> items, final Filter<? super T> filter) {
		assertNotNull(filter, "filter must not be null");
		assertNotNull(items, "items must not be null");

		return select(items, invert(filter));
	}

	public static <T> List<T> reverse(final Iterable<T> items) {
		assertNotNull(items, "items must not be null");
		List<T> result = asList(items);
		Collections.reverse(result);
		return result;
	}

	/**
	 * Filter a collection of <code>items</code>, returning only those that
	 * match a given <code>filter</code>, this is the inverse of reject.
	 * 
	 * @see #reject(Iterable, Filter)
	 */
	public static <T> List<T> select(final Iterable<T> items, final Filter<? super T> filter) {
		assertNotNull(filter, "filter must not be null");
		assertNotNull(items, "items must not be null");

		final List<T> selected = new ArrayList<T>();

		for (final T item : items) {
			if (filter.execute(item)) {
				selected.add(item);
			}
		}

		return selected;
	}

	/**
	 * Produce a single command that executes each of the given
	 * <code>commands</code> in sequence
	 */
	public static <T> Command<T> sequence(final Command<? super T>... commands) {
		return new Command<T>() {
			public void execute(final T value) {
				for (final Command<? super T> command : commands) {
					command.execute(value);
				}
			}
		};
	}

	/**
	 * Find the shortest list in a list of lists
	 * 
	 * @param collections
	 * @return the shortest list
	 */
	public static <U, T extends Collection<? extends U>> T shortest(final Iterable<T> collections) {
		assertNotNull(collections, "lists must not be null");
		assertTrue(hasItems(collections), "lists must have at least one item");
		return head(sort(asList(collections), COLLECTION_SIZE));
	}

	public static <T> boolean hasItems(final Iterable<T> iterable) {
		return iterable.iterator().hasNext();
	}

	public static <T> boolean isEmpty(final Iterable<T> iterable) {
		return !hasItems(iterable);
	}

	/**
	 * Create a list from the nth elements of the given lists.
	 */
	@SuppressWarnings("unchecked")
	public static List slice(final int n, final Iterable<List> items) {
		assertNotNull(items, "lists must not be null");
		assertGreaterThanOrEqualTo(0, n, "n must be greater than or equal to 0");
		final List result = new ArrayList();
		for (final List list : items) {
			result.add(list.get(n));
		}
		return result;
	}

	/**
	 * Create a list of string by splitting on a regex.
	 * 
	 * @see java.lang.String#split
	 */
	public static List<String> split(final String item, final String regex) {
		assertNotNull(item, "item must not be null");
		assertNotNull(regex, "regex must not be null");
		return asList(item.split(regex));
	}

	/**
	 * Get all item's (in iteration order) from a collection except the first.
	 * The collection must contain at least one item or an
	 * {@link jedi.assertion.AssertionError AssertionError} will be thrown.
	 * 
	 * @return all items except the first
	 * @throws jedi.assertion.AssertionError
	 *             if the collection contains less or more than one item
	 * @see #only(Collection)
	 * @see #headOrNullIfEmpty(Iterable)
	 * @see #headOrDefaultIfEmpty(Iterable, Object)
	 */
	public static <T> List<T> tail(final Iterable<T> items) {
		assertNotNull(items, "items must not be null");
		assertTrue(hasItems(items), "items must not be empty");

		return drop(1, items);
	}

	/**
	 * Get the first n elements of items.
	 * 
	 * @see <a
	 *      href="http://srfi.schemers.org/srfi-1/srfi-1.html#take">SRFI-1</a>
	 */
	public static <T> List<T> take(final int n, final Iterable<T> items) {
		assertNotNull(items, "list must not be null");
		List<T> result = new ArrayList<T>();
		for (T item : items) {
			if (result.size() == n) {
				return result;
			}
			result.add(item);
		}
		assertEqual(n, result.size(), "There are not enough items to take");
		return result;
	}

	/**
	 * Get the last n elements of an Iterable.
	 * 
	 * @see <a
	 *      href="http://srfi.schemers.org/srfi-1/srfi-1.html#take-right">SRFI-1</a>
	 */
	public static <T> List<T> takeRight(final int n, final Iterable<T> items) {
		assertNotNull(items, "list must not be null");
		List<T> asList = toList(items);
		assertLessThanOrEqualTo(asList.size(), n, "n must be less than or equal to list.size");

		return drop(asList.size() - n, asList);
	}

	/**
	 * Get n middle elements of an Iterable.
	 */
	public static <T> List<T> takeMiddle(final int start, final int n, final Iterable<T> items) {
		assertNotNull(items, "list must not be null");
		assertGreaterThanOrEqualTo(0, start, "start must not be negative");
		assertGreaterThanOrEqualTo(0, n, "n must not be negative");

		List<T> result = new ArrayList<T>();
		int index = 0;
		for (T item : items) {
			if (result.size() == n) {
				return result;
			}
			if (index >= start) {
				result.add(item);
			}
			index++;
		}

		assertEqual(n, result.size(), "The given items has insufficient elements");
		return result;
	}

	/**
	 * Zip interleaves a collection of lists. If zip is passed n lists, it
	 * returns a list as long as the shortest of these lists, each element of
	 * which is an n-element list comprised of the corresponding elements from
	 * the parameter lists.
	 * 
	 * <p/>
	 * See <a href="http://srfi.schemers.org/srfi-1/srfi-1.html#zip">SRFI-1</a>
	 * 
	 * @param lists
	 * @return zipped lists
	 */
	@SuppressWarnings("unchecked")
	public static List zip(final Iterable<List> lists) {
		final int n = shortest(lists).size();
		final List result = new ArrayList();
		for (int i = 0; i < n; i++) {
			result.add(slice(i, lists));
		}
		return result;
	}

	/**
	 * @return a list of lists by splitting the given list into lists of length
	 *         <code>length</code>.
	 */
	public static <T> List<List<T>> tabulate(final Iterable<T> list, int length) {
		List<T> line = toList(list);
		assertTrue(line.size() % length == 0, "size must be a multiple of length", line, length);
		List<List<T>> result = new ArrayList<List<T>>(line.size());
		for (int i = 0; i < line.size(); i += length) {
			result.add(takeMiddle(i, length, line));
		}
		return result;
	}

	/**
	 * Removes the last item (in iteration order) from a collection. The
	 * collection must contain at least one item or an
	 * {@link jedi.assertion.AssertionError AssertionError} will be thrown.
	 * Emulates Array.pop in Ruby.
	 * 
	 * @return the last item in the collection
	 * @throws jedi.assertion.AssertionError
	 *             if the collection contains less or more than one item
	 * @see #only(Collection)
	 * @see #headOrNullIfEmpty(Iterable)
	 * @see #headOrDefaultIfEmpty(Iterable, Object)
	 */
	public static <T> T pop(Iterable<T> items) {
		List<T> list = toList(items);
		assertNotNullOrEmpty(list, "items must not be null or empty");
		return list.remove(list.size() - 1);
	}

	/**
	 * Removes the last item (in iteration order) from a collection as an
	 * {@link Option}.
	 * 
	 * @return the last item in the collection as {@link Some} or {@link None}
	 *         if the list is empty.
	 */
	public static <T> Option<T> popOption(Iterable<T> items) {
		return isEmpty(items) ? Options.<T> none() : some(pop(items));
	}

	/**
	 * Partition a collection of items into two sublists using the given filter.
	 * 
	 * @param items
	 * @return a list whose first element as a list of items in list that pass
	 *         the filter, the second item is a list of elements that did not
	 *         pass the filter.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<List<T>> partition(Iterable<T> items, Filter<T> filter) {
		return list(select(items, filter), select(items, invert(filter)));
	}

	/**
	 * Return the set of all subsets of the provided collection.
	 *
	 * <p>For example, if all is the list ("x","y","z"), the return will be set containing
	 * 8 lists as follows:</p>
	 *
	 * <ul>
	 * 	<li>() - empty set</li>
	 * 	<li>("x")</li>
	 * 	<li>("y")</li>
	 * 	<li>("z")</li>
	 * 	<li>("x","y")</li>
	 * 	<li>("x","z")</li>
	 * 	<li>("y","z")</li>
	 * 	<li>("x","y","z")</li>
	 * </ul>
	 *
	 * <p>See <a href="http://en.wikipedia.org/wiki/Power_set">http://en.wikipedia.org/wiki/Power_set</a></p>
	 * 
	 * @see #powerset(Collection, Command)
	 * @see #foldPowerset(Object, Collection, Functor2)
	 *
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<List<T>> powerset(Collection<T> all) {
		assertNotNull(all, "all must not be null");
		return all.isEmpty() ? set(Collections.<T>emptyList()) : nonDegeneratePowerSet(all);
	}

	private static <T> Set<List<T>> nonDegeneratePowerSet(Collection<T> all) {
		Set<List<T>> powerset = new HashSet<List<T>>();
		int howManySubsetsInThePowerSet = 2 << (all.size() - 1);
		for(int i = 0 ; i != howManySubsetsInThePowerSet ; i++) {
			List<T> currentSubset = createSubsetFromOriginalSet(all, i);
			powerset.add(currentSubset);
		}
		return powerset;
	}

	private static <T> List<T> createSubsetFromOriginalSet(Iterable<T> all, int elementsToIncludeBitMap) {
		List<T> current = new ArrayList<T>();
		int remainingElementsToInclude = elementsToIncludeBitMap;
		for (T t : all) {
			if ((remainingElementsToInclude & 1) == 1) {
				current.add(t);
			}
			remainingElementsToInclude >>= 1;
		}
		return current;
	}

	/**
	 * Iterate all powersets of the give collection, in no particular order.
	 * @see #powerset(Collection)
	 * @see #foldPowerset(Object, Collection, Functor2)
	 */
	public static <T> void powerset(Collection<T> all, Command<List<? super T>> command) {
		assertNotNull(all, "all must not be null");
		assertNotNull(command, "command must not be null");
		recursePowerset(Collections.<T>emptyList(), asList(all), command);
	}

	@SuppressWarnings("unchecked")
	private static <T> void recursePowerset(List<T> prefix, List<T> tail, Command<List<? super T>> command) {
		command.execute(prefix);
		for (int i = 0 ; i < tail.size() ; i++) {
			recursePowerset(append(prefix, list(tail.get(i))), tail.subList(i + 1, tail.size()), command);
		}
	}

	/**
	 * Fold over all powersets of the give collection, in no particular order.
	 * @see #powerset(Collection)
	 * @see #powerset(Collection, Command)
	 * @see #fold(Object, Iterable, Functor2)
	 */
	public static <T, R, I extends R> R foldPowerset(I initialValue, Collection<T> all, final Functor2<R, List<? super T>, R> functor2) {
		assertNotNull(all, "all must not be null");
		assertNotNull(functor2, "functor2 must not be null");
		return recursePowerset(initialValue, Collections.<T>emptyList(), asList(all), functor2);
	}

	@SuppressWarnings("unchecked")
	private static <T, R, I extends R> R recursePowerset(I initialValue, List<T> prefix, List<T> tail, final Functor2<R, List<? super T>, R> functor2) {
		R value = functor2.execute(initialValue, prefix);
		for (int i = 0 ; i < tail.size() ; i++) {
			value = recursePowerset(value, append(prefix, list(tail.get(i))), tail.subList(i + 1, tail.size()), functor2);
		}
		return value;
	}
}
