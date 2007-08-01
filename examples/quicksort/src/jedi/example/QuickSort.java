package jedi.example;

import static jedi.functional.Coercions.*;
import static jedi.functional.Comparables.*;
import static jedi.functional.FirstOrderLogic.*;
import static jedi.functional.FunctionalPrimitives.*;

import java.util.Comparator;
import java.util.List;

public class QuickSort {

    @SuppressWarnings("unchecked")
    public static <T> List<T> sort(final List<T> collection, final Comparator<T> comparator) {
        if (collection.size() < 2) {
            return asList(collection);
        }

        final T head = head(collection);
        final List<T> tail = tail(collection);
        final List<T> lower = select(tail, lessThan(head, comparator));
        final List<T> upper = select(tail, not(lessThan(head, comparator)));
        return append(sort(lower, comparator), list(head), sort(upper, comparator));
    }

}
