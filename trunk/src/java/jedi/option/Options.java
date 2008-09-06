package jedi.option;

/**
 * A factory of option types for convenience and static importing.
 */
public final class Options {

	public static <T> Option<T> Some(T value) {
		return new Some<T>(value);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Option<T> None() {
		return (Option<T>) None.NONE;
	}
}
