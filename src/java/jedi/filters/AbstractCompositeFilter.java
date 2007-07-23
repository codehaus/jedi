package jedi.filters;

import static jedi.assertion.Assert.assertNotNull;
import static jedi.functional.Coercions.asSet;
import static jedi.functional.FunctionalPrimitives.join;
import jedi.functional.Filter;

public abstract class AbstractCompositeFilter<T> implements Filter<T> {
    private Filter<T>[] components;

    public AbstractCompositeFilter(Filter<T>... components) {
        assertNotNull(components, "components");
        this.components = components;
    }

    protected Filter<T>[] getComponents() {
        return components;
    }

    @Override
	public int hashCode() {
        return asSet(components).hashCode();
    }

    @SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }

        AbstractCompositeFilter<T> that = (AbstractCompositeFilter<T>) obj;

        return asSet(getComponents()).equals(asSet(that.getComponents()));
    }

    protected abstract String getFunctionName();

    @Override
	public String toString() {
        return getFunctionName() + "(" + join(asSet(getComponents()), ", ") + ")";
    }
}
