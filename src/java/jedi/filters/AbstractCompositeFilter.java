package jedi.filters;

import static jedi.assertion.Assert.*;
import static jedi.functional.Coercions.*;
import jedi.functional.Filter;
import static jedi.functional.FunctionalPrimitives.*;

public abstract class AbstractCompositeFilter<T> implements Filter<T> {
    private Filter<T>[] components;

    public AbstractCompositeFilter(Filter<T>... components) {
        assertNotNull(components, "components");
        this.components = components;
    }

    protected Filter<T>[] getComponents() {
        return components;
    }

    public int hashCode() {
        return asSet(components).hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }

        AbstractCompositeFilter that = (AbstractCompositeFilter) obj;

        return asSet(getComponents()).equals(asSet(that.getComponents()));
    }

    protected abstract String getFunctionName();

    public String toString() {
        return getFunctionName() + "(" + join(asSet(getComponents()), ", ") + ")";
    }
}
