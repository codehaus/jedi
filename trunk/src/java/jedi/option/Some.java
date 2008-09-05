package jedi.option;

public final class Some<T> extends AbstractOption<T> {
	
	private final T value;

	public Some(T value) {
		this.value = value;
	}
	
	public T get() {
		return value;
	}

	public void match(OptionMatcher<T> matcher) {
		matcher.caseSome(value);
	}

	public boolean isEmpty() {
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (obj instanceof Some) {
			Some some = (Some) obj;
			return get().equals(some.get());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return get().hashCode();
	}
	
	@Override
	public String toString() {
		return "Some: " + get();
	}
}
