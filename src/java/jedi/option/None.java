package jedi.option;

public final class None<T> extends AbstractOption<T> {

	public void match(OptionMatcher<T> matcher) {
		matcher.caseNone(this);
	}

	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof None;
	}
	
	@Override
	public String toString() {
		return "None";
	}
	
}
