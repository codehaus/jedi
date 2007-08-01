package jedi.filters;

import static jedi.assertion.Assert.*;
import static jedi.functional.Coercions.*;
import static jedi.functional.FunctionalPrimitives.*;
import jedi.functional.Filter;

public abstract class AbstractCompositeFilter<T> implements Filter<T> {
    private final Filter<T>[] components;

    public AbstractCompositeFilter(final Filter<T>... components) {
        assertNotNull(components, "components");
        this.components = components;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }

        final AbstractCompositeFilter<T> that = (AbstractCompositeFilter<T>) obj;

        return asSet(getComponents()).equals(asSet(that.getComponents()));
    }

    protected Filter<T>[] getComponents() {
        return components;
    }

    protected abstract String getFunctionName();

    @Override
    public int hashCode() {
        return asSet(components).hashCode();
    }

    @Override
    public String toString() {
        return getFunctionName() + "(" + join(asList(getComponents()), ", ") + ")";
    }
}
