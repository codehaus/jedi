package jedi.filters;

import jedi.functional.Filter;

public class Disjunction<T> extends AbstractCompositeFilter<T> {
	public static <T> Disjunction<T> create(Filter<T>... components) {
		return new Disjunction<T>(components);
	}

	public Disjunction(Filter<T>... components) {
		super(components);
	}

	public Boolean execute(T value) {
		for (Filter<T> filter : getComponents()) {
			if (filter.execute(value)) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected String getFunctionName() {
		return "or";
	}
}
