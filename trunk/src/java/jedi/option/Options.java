package jedi.option;

/**
 * A factory of option types for convenience and static importing.
 */
public final class Options {

	public static <T> Option<T> Some(T value) {
		return new Some<T>(value);
	}
	
	public static <T> Option<T> None() {
		return new None<T>();
	}
}
