package jedi.filters;

import jedi.functional.Filter;

public class NotNullFilter<T> implements Filter<T> {
    public Boolean execute(T value) {
        return value != null;
    }

    public int hashCode() {
        return 1237;
    }

    public boolean equals(Object obj) {
        return obj instanceof NotNullFilter;
    }
}
