package jedi.filters;

import jedi.functional.Filter;

public class Inverter<T> extends AbstractUnaryFilter<T, Filter<T>> {
	public static <T> Inverter<T> create(Filter<T> inverted) {
		return new Inverter<T>(inverted);
	}

	public Inverter(Filter<T> inverted) {
		super(inverted);
	}

	public Boolean execute(T value) {
		return !getTestValue().execute(value);
	}

	@Override
	protected String getFunctionName() {
		return "not";
	}
}
