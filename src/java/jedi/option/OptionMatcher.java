package jedi.option;

/**
 * Receiver for Some or None in {@link Option#match(OptionMatcher)}.
 */
public interface OptionMatcher<T> {
	void caseSome(T value);
	void caseNone(None<T> none);
}
