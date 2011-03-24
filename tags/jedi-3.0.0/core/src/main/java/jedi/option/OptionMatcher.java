package jedi.option;

/**
 * Receiver for {@link Some} or {@link None} in {@link Option#match(OptionMatcher)}.
 */
public interface OptionMatcher<T> {

	/**
	 * The visited {@link Option} was a {@link Some}.
	 * @param value the value of the {@link Option}.
	 */
	void caseSome(T value);

	/**
	 * The visitied {@link Option} was a {@link None}.
	 */
	void caseNone();
}
