package jedi.example;

import static jedi.functional.Coercions.asList;
import static jedi.functional.Coercions.list;
import static jedi.functional.Comparables.lessThan;
import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.head;
import static jedi.functional.FunctionalPrimitives.partition;
import static jedi.functional.FunctionalPrimitives.tail;

import java.util.Comparator;
import java.util.List;

public class QuickSort {

    @SuppressWarnings("unchecked")
    public static <T> List<T> sort(final List<T> collection, final Comparator<T> comparator) {
    	if (collection.size() < 2) {
    		return asList(collection);
    	}
    	
    	final T head = head(collection);
    	List<List<T>> partitioned = partition(tail(collection), lessThan(head, comparator));
    	return append(
    			sort(partitioned.get(0), comparator), 
    			list(head), 
    			sort(partitioned.get(1), comparator));
    }

}
