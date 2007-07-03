package jedi.example;

import static jedi.functional.Coercions.*;
import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import jedi.functional.Comparables;

public class QuickSort {

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> sort(Collection<T> collection, Comparator<T> comparator) {
		if (collection.size() < 2) {
			return collection;
		}

		T head = head(collection);
		List<T> lower = select(collection, Comparables.lessThan(head, comparator));
		List<T> upper = select(collection, Comparables.greaterThan(head, comparator));
		return append(sort(lower, comparator), list(head), sort(upper, comparator));
	}

}
