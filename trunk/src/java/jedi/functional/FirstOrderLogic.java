package jedi.functional;

import static jedi.assertion.Assert.assertNotNull;
import static jedi.functional.Coercions.asList;
import static jedi.functional.Coercions.asSet;
import static jedi.functional.FunctionalPrimitives.head;
import static jedi.functional.FunctionalPrimitives.select;
import static jedi.functional.FunctionalPrimitives.tail;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jedi.filters.Conjunction;
import jedi.filters.Disjunction;
import jedi.filters.Inverter;


public class FirstOrderLogic {
    /**
     * Returns <code>true</code> if a given <code>predicate</code> returns <code>true</code> for at least one of
     * the <code>items</code> in a given collection. <code>false</code> otherwise.
     */
    public static <T> boolean exists(Collection<T> items, Filter<? super T> predicate) {
    	assertNotNull(items, "items must not be null");
        assertNotNull(predicate, "predicate must not be null");

        for (T item : items) {
            if (predicate.execute(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> if a given <code>predicate</code> returns <code>true</code> for all of the
     * <code>items</code> in a given collection. <code>false</code> otherwise.
     */
    public static <T> boolean all(Collection<T> items, Filter<? super T> predicate) {
        return !exists(items, invert(predicate));
    }

    /**
     * Returns <code>true</code> if a given <code>predicate</code> returns <code>true</code> for zero or one of
     * the <code>items</code> in a given collection. <code>false</code> otherwise.
     */
    public static <T> boolean zeroOrOne(Collection<T> items, Filter<? super T> predicate) {
        return select(items, predicate).size() <= 1;
    }

    /**
     * Create a filter which is the logical inversion of a given filter
     */
    public static <T> Filter<T> invert(final Filter<T> filter) {
        return Inverter.create(filter);
    }

    /**
     * Synonymous with {@link #invert(Filter) invert(Filter)}
     */
    public static <T> Filter<T> not(Filter<T> filter) {
        return invert(filter);
    }

    /**
     * Create a short circuiting filter which is the conjunction of the given <code>filters</code>
     */
    public static <T> Filter<T> and(final Filter<T>... filters) {
        return Conjunction.create(filters);
    }

    /**
     * Create a short circuiting filter which is the disjunction of the given <code>filters</code>
     */
    public static <T> Filter<T> or(final Filter<T>... filters) {
        return Disjunction.create(filters);
    }

    /**
     * Calculate the intersection of all of the given collections.
     */
    public static <T> Set<T> intersection(Collection<T>... collections) {
        return intersection(asList(collections));
    }
    
    /**
     * Calculate the intersection of all of the given collections.
     */
    public static <T> Set<T> intersection(Collection<Collection<T>> collections) {
        assertNotNull(collections, "collections");

        if (collections.isEmpty()) {
            return new HashSet<T>();
        }

        Set<T> intersection = asSet(head(collections));
        for (Collection<T> collection : tail(collections)) {
            intersection.retainAll(collection);
        }

        return intersection;
    }
    
    /**
     * Calculate the union of all of the given collections.
     */
    public static <T> Set<T> union(Collection<T>... collections) {
        return union(asList(collections));
    }
    
    /**
     * Calculate the union of all of the given collections.
     */
    public static <T> Set<T> union(Collection<Collection<T>> collections) {
        assertNotNull(collections, "collections");

        if (collections.isEmpty()) {
            return new HashSet<T>();
        }

        Set<T> union = asSet(head(collections));
        for (Collection<T> collection : tail(collections)) {
        	union.addAll(collection);
        }

        return union;
    }


    private FirstOrderLogic() {
    }
}
