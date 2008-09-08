package jedi.option;

import java.util.Map;

/**
 * A factory of option types for convenience and static importing, and other utility methods.
 */
public final class Options {

	/**
	 * Factory method for Some.
	 * @return Some(value)
	 */
	public static <T> Option<T> Some(T value) {
		return new Some<T>(value);
	}
	
	/**
	 * Factory method for None.
	 * @return None
	 */
	public static <T> Option<T> None() {
		return new None<T>();
	}

	/**
	 * @return Some(value) or None if value is null
	 */
	public static <T> Option<T> option(T value) {
		return value == null ? Options.<T>None() : Some(value);
	}
	
	/**
	 * Get an Option from a Map.
	 * @param map the map to retrieve a value from
	 * @param key the key to use
	 * @return None if the map did not contain a value for <code>key</code>, Some(value) if the map did contain the value.
	 */
	public static <K, V> Option<V> get(Map<K, V> map, K key) {
		return map.containsKey(key) ? Some(map.get(key)) : Options.<V>None();
	}
}
