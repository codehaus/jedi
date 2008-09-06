package jedi.option;

import java.util.NoSuchElementException;

public final class None extends AbstractOption<Void> {
	
	public static final None NONE = new None();
	
	private None() {
	}
	
	public Void get() {
		throw new NoSuchElementException("None.get");
	}

	@SuppressWarnings("unchecked")
	public void match(OptionMatcher matcher) {
		matcher.caseNone(this);
	}

	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public String toString() {
		return "None";
	}
	
}
