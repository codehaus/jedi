package jedi.example;

import jedi.tuple.Tuple2;

import java.util.Comparator;
import java.util.List;

import static jedi.functional.Coercions.list;
import static jedi.functional.Comparables.lessThan;
import static jedi.functional.FunctionalPrimitives.*;

public class QuickSort {

	public static <T> List<T> sort(final List<T> collection, final Comparator<T> comparator) {
		if (collection.size() < 2) {
			return collection;
		}

		final T head = head(collection);
		Tuple2<List<T>, List<T>> partitioned = partition(tail(collection), lessThan(head, comparator));
		return append(sort(partitioned.a(), comparator), list(head), sort(partitioned.b(), comparator));
	}

}
