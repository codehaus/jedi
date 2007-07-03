package jedi.filters;

import jedi.functional.Filter;

public class Conjunction<T> extends AbstractCompositeFilter<T> {
    public static <T> Conjunction<T> create(Filter<T>... components) {
        return new Conjunction<T>(components);
    }

    public Conjunction(Filter<T>... components) {
        super(components);
    }

    public Boolean execute(T value) {
        for (Filter<T> filter : getComponents()) {
            if (!filter.execute(value)) {
                return false;
            }
        }

        return true;
    }

    protected String getFunctionName() {
        return "and";
    }
}
