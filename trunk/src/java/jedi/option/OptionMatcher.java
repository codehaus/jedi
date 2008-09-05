package jedi.option;

public interface OptionMatcher<T> {
	void caseSome(T value);
	void caseNone(None<T> none);
}
