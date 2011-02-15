package jedi.filters;

import jedi.functional.Filter;
import static jedi.assertion.Assert.assertNotNull;

public abstract class AbstractUnaryFilter<T, U> implements Filter<T> {
	private U testValue;

	public AbstractUnaryFilter(U testValue) {
		assertNotNull(testValue, "testValue must not be null");
		this.testValue = testValue;
	}

	protected U getTestValue() {
		return testValue;
	}

	@Override
	public int hashCode() {
		return testValue.hashCode();
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

		return getTestValue().equals(((AbstractUnaryFilter<T, U>) obj).getTestValue());
	}

	@Override
	public String toString() {
		return getFunctionName() + "(" + getTestValue() + ")";
	}

	protected abstract String getFunctionName();
}
