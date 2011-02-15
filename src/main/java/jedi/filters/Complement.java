package jedi.filters;

import static jedi.assertion.Assert.assertNotNull;
import jedi.functional.Filter;

public class Complement<T> implements Filter<T> {

	private final Iterable<T> iterable;

	public static <T> Complement<T> minus(Iterable<T> iterable) {
		return new Complement<T>(iterable);
	}

	private Complement(Iterable<T> iterable) {
		assertNotNull(iterable, "iterable can not be null");
		this.iterable = iterable;
	}

	public Boolean execute(T a) {
		return !contains(a);
	}

	private Boolean contains(T a) {
		for (T b : iterable) {
			if (a.equals(b)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("minus %s", iterable);
	}
}