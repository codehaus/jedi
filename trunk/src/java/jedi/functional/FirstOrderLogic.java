package jedi.functional;

import static jedi.assertion.Assert.assertNotNull;
import static jedi.functional.FunctionalPrimitives.select;

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
        assertNotNull(predicate, "predicate");
        assertNotNull(items, "items");

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
        assertNotNull(collections, "collections");

        if (collections.length == 0) {
            return new HashSet<T>();
        }

        Set<T> intersection = Coercions.asSet(collections[0]);
        for (int i = 1; i < collections.length; i++) {
            intersection.retainAll(collections[i]);
        }

        return intersection;
    }


    private FirstOrderLogic() {
    }
}
