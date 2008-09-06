package jedi.option;

import java.util.Map;

/**
 * A factory of option types for convenience and static importing, and other utility methods.
 */
public final class Options {

	public static <T> Option<T> Some(T value) {
		return new Some<T>(value);
	}
	
	public static <T> Option<T> None() {
		return new None<T>();
	}

	public static <K, V> Option<V> get(Map<K, V> map, K key) {
		return map.containsKey(key) ? Some(map.get(key)) : Options.<V>None();
	}
}