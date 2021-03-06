package jedi.functional;

import jedi.filters.AllPassFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a range of ints, with a possible filter.
 * <p>Instances of this class are {@link Serializable} if the given filter is {@link Serializable}.</p>
 */
public class Range implements Iterable<Integer>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int start;
    private final int end;
    private final Filter<Integer> filter;

    public static Range range(int start, int end) {
        return new Range(start, end);
    }

    public Range(int start, int end) {
        this(start, end, new AllPassFilter<Integer>());
    }

    public Range(int start, int end, Filter<Integer> filter) {
        this.start = start;
        this.end = end;
        this.filter = filter;
    }

    public Iterator<Integer> iterator() {
        List<Integer> items = new ArrayList<Integer>();
		for (int i = start; i < end; i++) {
            if (filter.execute(i)) items.add(i);
        }
        return items.iterator();
    }
}
