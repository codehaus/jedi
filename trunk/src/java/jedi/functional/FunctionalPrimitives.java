package jedi.functional;

import static jedi.assertion.Assert.*;
import static jedi.functional.Coercions.*;
import static jedi.functional.FirstOrderLogic.*;

import java.util.*;

/**
 * I provide operations of the kind found in Functional Programming languages. This allows you to remove a great deal of clutter from production code. Ideally, you will never need
 * to write another 'for' loop again, and a great deal of explicit conditional logic should be removable as well. <p/>Generally, functions that transform collections (using
 * functors or whatever) preserve the iteration order of the given collection in the result.
 */
public class FunctionalPrimitives {
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

    /**
     * Append all of the elements in all of the given <code>collections</code> into one list. All of the elements of the first item in <code>collections</code> are appended
     * first, then the items in the second collection, etc.
     *
     * @see #append(Collection[])
     */
    public static <T> List<T> append(final Collection< ? extends Collection< ? extends T>> collections) {
        return flatten(collections, new Functor<Collection< ? extends T>, Collection<T>>() {
            @SuppressWarnings("unchecked")
            public Collection<T> execute(final Collection< ? extends T> value) {
                return (Collection<T>) value;
            }
        });
    }

    /**
     * Append all of the elements in all of the given <code>collections</code> into one list. All of the elements of the first item in <code>collections</code> are appended
     * first, then the items in the second collection, etc. Equivalent to <code>append(list(collections))</code>
     *
     * @see #append(Collection)
     */
    public static <T> List<T> append(final Collection< ? extends T>... collections) {
        return append(list(collections));
    }

    /**
     * Apply <code>functor</code> to each element of <code>items</code> and return the list of results.
     *
     * @see #collect(Object[],Functor)
     */
    public static <T, R> List<R> collect(final Collection<T> items, final Functor< ? super T, R> functor) {
        assertNotNull(items, "items");
        assertNotNull(functor, "functor");

        final List<R> mapped = new ArrayList<R>(items.size());

        for (final T item : items) {
            mapped.add(functor.execute(item));
        }

        assertEqual(items.size(), mapped.size(), "same size");

        return mapped;
    }

    /**
     * Apply <code>functor</code> to each element of <code>items</code> and return the list of results. The iteration order of <code>items</code> is preserved in the returned
     * list. <p/> Equivalent to <code>collect(functor, asList(items))</code>
     *
     * @see #collect(Collection,Functor)
     */
    public static <T, R> List<R> collect(final T[] items, final Functor< ? super T, R> functor) {
        return collect(asList(items), functor);
    }

    /**
     * Get all but the first n elements of <code>items<code>. See <a href="http://srfi.schemers.org/srfi-1/srfi-1.html#drop">SRFI-1</a>
     */
    public static <T> List<T> drop(final int n, final Collection<T> items) {
        assertNotNull(items, "list");
        assertLessThanOrEqualTo(items.size(), n, "n <= list size");
        return asList(asList(items).subList(n, items.size()));
    }

    /**
     * Get all but the last n elements of <code>items</code>. See <a href="http://srfi.schemers.org/srfi-1/srfi-1.html#drop-right">SRFI-1</a>
     */
    public static <T> List<T> dropRight(final int n, final Collection<T> items) {
        assertNotNull(items, "list");
        assertLessThanOrEqualTo(items.size(), n, "n <= list size");
        return take(items.size() - n, items);
    }

    /**
     * Suppose there is a collection of items (c1, c2, c3), each of which contains a collection <i>i.e.</i> (c1 = (c1_1, c1_2, ...), c2=(c2_1, c2_2, ...). I can produce a
     * collection containing all of the 'leaf' items <i>i.e.</i>(c1_1, c1_2, ..., c2_1, c2_2)
     *
     * @param items
     *            The collection of items containing the collection of leaves
     * @param functor
     *            Given an element of the 'top' collection, this can obtain the collection of 'leaf' objects to accumulate
     */
    public static <T, R> List<R> flatten(final Collection<T> items, final Functor< ? super T, ? extends Collection< ? extends R>> functor) {
        assertNotNull(functor, "functor");
        assertNotNull(items, "items");

        final List<R> list = new ArrayList<R>();

        for (final T t : items) {
            list.addAll(functor.execute(t));
        }

        return list;
    }

    /**
     * Fold passes each item of a collection with an accumulated value to a functor. <p/> For example, to sum the elements of a list: <p/>
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
     * <p/>For a more comprehensive description, see <a href="http://srfi.schemers.org/srfi-1/srfi-1.html#FoldUnfoldMap">SRFI-1</a>
     */
    public static <T, R, I extends R> R fold(final I initialValue, final Collection<T> collection, final Functor2<R, ? super T, R> functor2) {
        assertNotNull(collection, "collection");
        assertNotNull(functor2, "functor2");

        R accumulated = initialValue;
        for (final T t : collection) {
            accumulated = functor2.execute(accumulated, t);
        }
        return accumulated;
    }

    /**
     * Iterate over a collection of <code>items</code> applying the given <code>command</code> to each
     */
    public static <T> Collection<T> forEach(final Collection<T> items, final Command< ? super T> command) {
        assertNotNull(command, "command");
        assertNotNull(items, "items");

        for (final T item : items) {
            command.execute(item);
        }

        return items;
    }

    /**
     * Group the elements of a collection such that all elements that are mapped to the same value by a given <code>keyFunctor</code> are in the same group. The groups are
     * returned as a map in which each value is a collection of values in one group and whose key is the value returned by the <code>keyFunctor</code> for all items in the group.
     */
    public static <K, V> Map<K, List<V>> group(final Collection<V> toGroup, final Functor< ? super V, K> keyFunctor) {
        assertNotNull(keyFunctor, "keyFunctor");
        assertNotNull(toGroup, "toGroup");

        final Map<K, List<V>> groups = new HashMap<K, List<V>>();

        for (final V v : toGroup) {
            addToGroup(keyFunctor.execute(v), v, groups);
        }

        return groups;
    }

    /**
     * Get the first item (in iteration order) from a collection. The collection must contain at least one item or an {@link jedi.assertion.AssertionError AssertionError} will be
     * thrown.
     *
     * @return the first item in the collection
     * @throws jedi.assertion.AssertionError
     *             if the collection contains less or more than one item
     * @see #only(Collection)
     * @see #headOrNullIfEmpty(Collection)
     * @see #headOrDefaultIfEmpty(Collection,Object)
     */
    public static <T> T head(final Collection<T> items) {
        assertNotNull(items, "items");
        assertFalse(items.isEmpty(), "items not empty");
        return headOrNullIfEmpty(items);
    }

    /**
     * Get the first item (in iteration order) from a collection or <code>defaultValue</code> (which may be null) if the collection is empty.
     *
     * @return the first item in the collection or <code>defaultValue</code> if the collection is empty
     * @see #only(Collection)
     * @see #head(Collection)
     * @see #headOrNullIfEmpty(Collection)
     */
    public static <T> T headOrDefaultIfEmpty(final Collection< ? extends T> items, final T defaultValue) {
        assertNotNull(items, "items");

        if (items.isEmpty()) {
            return defaultValue;
        }

        return (items instanceof List ? (List< ? extends T>) items : asList(items)).get(0);
    }

    /**
     * Get the first item (in iteration order) from a collection or <code>null</code> if the collection is empty.
     *
     * @return the first item in the collection or null if the collection is empty
     * @see #only(Collection)
     * @see #head(Collection)
     * @see #headOrDefaultIfEmpty(Collection,Object)
     */
    public static <T> T headOrNullIfEmpty(final Collection<T> items) {
        return headOrDefaultIfEmpty(items, null);
    }

    /**
     * An alias for fold.
     *
     * @see #fold(Object, java.util.Collection, Functor2)
     */
    public static <T, R, I extends R> R inject(final I initialValue, final Collection<T> collection, final Functor2<R, ? super T, R> functor2) {
        return fold(initialValue, collection, functor2);
    }

    /**
     * Join, with  default delimiter (empty string)
     *
     * @see #join(Object[], String)
     */
    public static String join(final Collection< ? > items) {
        return join(items, "");
    }

    public static String join(final Collection< ? > items, final String delimiter) {
        assertNotNull(items, "items");
        return join(items.toArray(), delimiter);
    }

    /**
     * Returns a string created by converting each element of an array to a string, separated by delimiter.  Emulates Array.join in Ruby.
     */
    public static String join(final Object[] items, final String delimiter) {
        assertNotNull(items, "items");
        assertNotNull(delimiter, "delimiter");

        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < items.length; i++) {
            sb.append(items[i]);
            if (i + 1 < items.length) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    /**
     * Returns an n-element list. Element i of the list, where 0 <= i < n, is produced by the functor. For a more comprehensive description, see <a
     * href="http://srfi.schemers.org/srfi-1/srfi-1.html#list-tabulate">SRFI-1</a>
     *
     * @param n
     *            the length of the list
     * @param functor
     *            the functor taking an integer and returning an <R>
     */
    public static <R> List<R> listTabulate(final int n, final Functor<Integer, R> functor) {
        assertNotNull(functor, "functor");
        final List<R> list = new ArrayList<R>(n);
        for (int i = 0; i < n; i++) {
            list.add(functor.execute(i));
        }
        return list;
    }

    /**
     * Find the longest list in a list of lists
     *
     * @param lists
     * @return the shortest list
     */
    public static <U, T extends Collection< ? extends U>> T longest(final Collection<T> lists) {
        assertNotNull(lists, "lists");
        assertGreaterThanOrEqualTo(1, lists.size(), "lists should have at least one item");
        return head(reverse(Comparables.sortInPlace(asList(lists), COLLECTION_SIZE)));
    }

    /**
     * Returns an n-element list, whose elements are all the value fill. For a more comprehensive description, see <a
     * href="http://srfi.schemers.org/srfi-1/srfi-1.html#make-list">SRFI-1</a>
     */
    public static <T> List<T> makeList(final int n, final T fill) {
        assertNotNull(fill, "fill");
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
     * @see #head(Collection)
     * @see #headOrNullIfEmpty(Collection)
     */
    public static <T> T only(final Collection<T> items) {
        assertNotNull(items, "items");
        assertEqual(1, items.size(), "only one item");
        return head(items);
    }

    /**
     * Create the Cartesian product of two collections, using a {@link Functor2 functor} as a factory of objects to represent the pair-wise products.
     */
    public static <T, U, R> List<R> produce(final Collection<T> left, final Collection<U> right, final Functor2< ? super T, ? super U, R> factory) {
        final List<R> product = new ArrayList<R>(left.size() * right.size());

        for (final T t : left) {
            for (final U u : right) {
                product.add(factory.execute(t, u));
            }
        }

        return product;
    }

    /**
     * Filter a collection of <code>items</code>, returning only those that do not match a given <code>filter</code>, this is the inverse of select.
     *
     * @see #select(java.util.Collection, Filter)
     */
    public static <T> List<T> reject(final Collection<T> items, final Filter< ? super T> filter) {
        assertNotNull(filter, "filter");
        assertNotNull(items, "items");

        return select(items, invert(filter));
    }

    public static <T> List<T> reverse(final Collection<T> items) {
        assertNotNull(items, "items");
        final List<T> result = asList(items);
        Collections.reverse(result);
        return result;
    }

    /**
     * Filter a collection of <code>items</code>, returning only those that match a given <code>filter</code>, this is the inverse of reject.
     *
     * @see #reject(java.util.Collection, Filter)
     */
    public static <T> List<T> select(final Collection<T> items, final Filter< ? super T> filter) {
        assertNotNull(filter, "filter");
        assertNotNull(items, "items");

        final List<T> selected = new ArrayList<T>();

        for (final T item : items) {
            if (filter.execute(item)) {
                selected.add(item);
            }
        }

        return selected;
    }

    /**
     * Produce a single command that executes each of the given <code>commands</code> in sequence
     */
    public static <T> Command<T> sequence(final Command< ? super T>... commands) {
        return new Command<T>() {
            public void execute(final T value) {
                for (final Command< ? super T> command : commands) {
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
    public static <U, T extends Collection< ? extends U>> T shortest(final Collection<T> collections) {
        assertNotNull(collections, "lists");
        assertGreaterThanOrEqualTo(1, collections.size(), "lists should have at least one item");
        return head(Comparables.sortInPlace(asList(collections), COLLECTION_SIZE));
    }

    /**
     * Create a list from the nth elements of the lists.
     */
    @SuppressWarnings("unchecked")
    public static List slice(final int n, final List<List> lists) {
        assertNotNull(lists, "lists");
        assertGreaterThanOrEqualTo(0, n, "i >= 0");
        final List items = new ArrayList();
        for (final List list : lists) {
            items.add(list.get(n));
        }
        return items;
    }

    public static List<String> split(final String item, final String regex) {
        assertNotNull(item, "item");
        assertNotNull(regex, "regex");
        return asList(item.split(regex));
    }

    /**
     * Get all item's (in iteration order) from a collection except the first. The collection must contain at least one item or an
     * {@link jedi.assertion.AssertionError AssertionError} will be thrown.
     *
     * @return all items except the first
     * @throws jedi.assertion.AssertionError
     *             if the collection contains less or more than one item
     * @see #only(Collection)
     * @see #headOrNullIfEmpty(Collection)
     * @see #headOrDefaultIfEmpty(Collection,Object)
     */
    public static <T> List<T> tail(final Collection<T> items) {
        assertNotNull(items, "items");
        assertFalse(items.isEmpty(), "items not empty");

        return drop(1, items);
    }

    /**
     * Get the first n elements of list. See <a href="http://srfi.schemers.org/srfi-1/srfi-1.html#take">SRFI-1</a>
     */
    public static <T> List<T> take(final int n, final Collection<T> items) {
        assertNotNull(items, "list");
        assertLessThanOrEqualTo(items.size(), n, "n <= list size");
        return asList(asList(items).subList(0, n));
    }

    /**
     * Get the last n elements of list. See <a href="http://srfi.schemers.org/srfi-1/srfi-1.html#take-right">SRFI-1</a>
     */
    public static <T> List<T> takeRight(final int n, final Collection<T> list) {
        assertNotNull(list, "list");
        assertLessThanOrEqualTo(list.size(), n, "n <= list size");

        return drop(list.size() - n, list);
    }

    /**
     * Get n middle elements of a list.
     */
    public static <T> List<T> takeMiddle(final int start, final int n, final Collection<T> list) {
    	assertNotNull(list, "list");
    	assertLessThanOrEqualTo(list.size(), start + n, "from + n <= list size");

    	return take(n, drop(start, list));
    }

    /**
     * Zip interleaves a list of lists. If zip is passed n lists, it returns a list as long as the shortest of these lists, each element of which is an n-element list comprised of
     * the corresponding elements from the parameter lists.
     *
     * <p/>See <a href="http://srfi.schemers.org/srfi-1/srfi-1.html#zip">SRFI-1</a>
     *
     * @param lists
     * @return zipped lists
     */
    @SuppressWarnings("unchecked")
    public static List zip(final List<List> lists) {
        final int n = shortest(lists).size();
        final List result = new ArrayList();
        for (int i = 0; i < n; i++) {
            result.add(slice(i, lists));
        }
        return result;
    }

    /**
     * @param <T>
     * @param line
     * @param length
     * @return a list of lists by splitting the given list into lists of length <code>length</code>.
     */
    public static <T> List<List<T>> tabulate(final List<T> line, int length) {
    	assertTrue(line.size() % length == 0, "list length is a multiple of required length", line, length);
    	List<List<T>> result = new ArrayList<List<T>>();
    	for (int i = 0; i < line.size(); i+=length) {
    		result.add(takeMiddle(i,length, line));
    	}
    	return result;
    }

    /**
     * Removes the last item (in iteration order) from a collection. The collection must contain at least one item or an {@link jedi.assertion.AssertionError AssertionError} will be
     * thrown.  Emulates Array.pop in Ruby.
     *
     * @return the last item in the collection
     * @throws jedi.assertion.AssertionError
     *             if the collection contains less or more than one item
     * @see #only(Collection)
     * @see #headOrNullIfEmpty(Collection)
     * @see #headOrDefaultIfEmpty(Collection,Object)
     */

    public static <T> T pop(List<T> items) {
        assertNotNullOrEmpty(items, "items");
        return items.remove(items.size() - 1);
    }

    protected FunctionalPrimitives() {
    }
}
