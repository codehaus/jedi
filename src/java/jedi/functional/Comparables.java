package jedi.functional;

import static jedi.assertion.Assert.*;
import static jedi.functional.Coercions.*;
import static jedi.functional.FunctionalPrimitives.*;
import jedi.functors.MaxFunctor;
import jedi.functors.MinFunctor;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Comparables {

    public static <T> Filter<T> lessThan(final T item, final Comparator<T> comparator) {
		return new Filter<T>() {
			public Boolean execute(T value) {
				return comparator.compare(value, item) < 0;
			}
		};
	}

	public static <T> Filter<T> equal(final T item, final Comparator<T> comparator) {
		return new Filter<T>() {
			public Boolean execute(T value) {
				return comparator.compare(value, item) == 0;
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static <T> Filter<T> greaterThan(final T item, final Comparator<T> comparator) {
		return FirstOrderLogic.not(FirstOrderLogic.or(Comparables.lessThan(item, comparator), Comparables.equal(item, comparator)));
	}

	/**
	 * Find and return the maximum value from a collection of comparables. Requires at least
	 * one item in the collection.
	 */
	public static <T extends Comparable<? super T>> T max(Collection<T> items) {
	    return fold(head(items), items, new MaxFunctor<T>());
	}

	/**
	 * Find and return the minimum value from a collection of comparables. Requires at least
	 * one item in the collection.
	 */
	public static <T extends Comparable<? super T>> T min(Collection<T> items) {
	    return fold(head(items), items, new MinFunctor<T>());
	}

	/**
	 * Find and return the maximum value from a collection of comparables, or a default
	 * value if the collection is empty.
	 */
	public static <T extends Comparable<? super T>> T max(T defaultValue, Collection<T> items) {
	    assertNotNull(items, "items");
	    return items.isEmpty() ? defaultValue : Comparables.max(items);
	}

	/**
	 * Find and return the minimum value from a collection of comparables, or a default
	 * value if the collection is empty.
	 */
	public static <T extends Comparable<? super T>> T min(T defaultValue, Collection<T> items) {
	    assertNotNull(items, "items");
	    return items.isEmpty() ? defaultValue : Comparables.min(items);
	}

	/**
	 * Sort <code>items</code> in place using the given <code>comparator</code>. Note that this function returns
	 * the collection allowing functions to be composed easily.
	 */
	public static <T> List<T> sortInPlace(List<T> items, Comparator<? super T> comparator) {
	    assertNotNull(items, "items");
	    assertNotNull(comparator, "comparator");
	    Collections.sort(items, comparator);
	    return items;
	}

	/**
	 * Sort <code>items</code> in place using the {@link java.lang.Comparable Comparable} values obtained
	 * by applying the given <code>evaluator</code> to each item. Note that this function returns
	 * the collection allowing functions to be composed easily.
	 */
	public static <T, C extends Comparable<? super C>> List<T> sortInPlace(List<T> items, final Functor<? super T, C> evaluator) {
	    assertNotNull(items, "items");
	    assertNotNull(evaluator, "evaluator");
	    return sortInPlace(items, new Comparator<T>() {
	        public int compare(T t1, T t2) {
	            return evaluate(t1).compareTo(evaluate(t2));
	        }
	
	        private C evaluate(final T t) {
	            return evaluator.execute(t);
	        }
	    });
	}

	/**
	 * Sort <code>items</code> in place using the natural ordering of the contained items. Note that this function
	 * returns the collection allowing functions to be composed easily.
	 */
	public static <T extends Comparable<? super T>> List<T> sortInPlace(List<T> items) {
	    assertNotNull(items, "items");
	    Collections.sort(items);
	    return items;
	}

	/**
	 * Sort <code>items</code> using the given <code>comparator</code>.
	 */
	public static <T> List<T> sort(Collection<T> items, Comparator<? super T> comparator) {
	    assertNotNull(items, "items");
	    assertNotNull(comparator, "comparator");
	    
	    List<T> list = asList(items);
		Collections.sort(list, comparator);
	    return list;
	}

	/**
	 * Sort <code>items</code> in place using the {@link java.lang.Comparable Comparable} values obtained
	 * by applying the given <code>evaluator</code> to each item. Note that this function returns
	 * the collection allowing functions to be composed easily.
	 */
	public static <T, C extends Comparable<? super C>> List<T> sort(Collection<T> items, final Functor<? super T, C> evaluator) {
	    assertNotNull(items, "items");
	    
	    List<T> list = asList(items);
	    return sortInPlace(list, evaluator);
	}

	/**
	 * Sort <code>items</code> in place using the natural ordering of the contained items. Note that this function
	 * returns the collection allowing functions to be composed easily.
	 */
	public static <T extends Comparable<? super T>> List<T> sort(Collection<T> items) {
	    assertNotNull(items, "items");
	    
	    List<T> list = asList(items);
	    Collections.sort(list);
	    return list;
	}


    protected Comparables() {
    }
}
