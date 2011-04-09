package jedi.filters;

import java.io.Serializable;

import jedi.functional.Filter;

public class AllPassFilter<T> implements Filter<T>, Serializable {
	private static final long serialVersionUID = 1L;
	
	public Boolean execute(T value) {
		return true;
	}
}
