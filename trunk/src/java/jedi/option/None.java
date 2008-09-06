package jedi.option;

public final class None extends AbstractOption<Object> {
	
	public static final None NONE = new None();
	
	private None() {
		
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
