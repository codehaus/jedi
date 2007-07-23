package jedi.example;

import static jedi.functional.Coercions.list;
import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.head;
import static jedi.functional.FunctionalPrimitives.select;
import static jedi.functional.Comparables.lessThan;
import static jedi.functional.Comparables.greaterThan;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;


public class QuickSort {

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> sort(Collection<T> collection, Comparator<T> comparator) {
		if (collection.size() < 2) {
			return collection;
		}

		T head = head(collection);
		List<T> lower = select(collection, lessThan(head, comparator));
		List<T> upper = select(collection, greaterThan(head, comparator));
		return append(sort(lower, comparator), list(head), sort(upper, comparator));
	}

}
