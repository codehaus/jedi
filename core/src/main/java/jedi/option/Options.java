package jedi.option;

import java.util.Map;

/**
 * A factory of option types for convenience and static importing, and other
 * utility methods.
 */
public final class Options {

	/**
	 * Factory method for Some.
	 * 
	 * @return Some(value)
	 */
	public static <T> Option<T> some(T value) {
		return new Some<T>(value);
	}

	/**
	 * Factory method for None.
	 * 
	 * @return None
	 */
	public static <T> Option<T> none() {
		return new None<T>();
	}

	/**
	 * @return Some(value) or None if value is null
	 */
	public static <T> Option<T> option(T value) {
		return value == null ? Options.<T> none() : some(value);
	}

	/**
	 * Get an Option for a key's value in a Map.
	 * 
	 * @param map
	 *            the map to retrieve a value from
	 * @param key
	 *            the key to use
	 * @return None if the map does not contain a value for <code>key</code> or the value is <code>null</code>.
	 *         Some(value) if the map did contain a non-<code>null</code> value for <code>key</code>.
	 */
	public static <K, V> Option<V> get(Map<K, V> map, K key) {
		return option(map.get(key));
	}
}
