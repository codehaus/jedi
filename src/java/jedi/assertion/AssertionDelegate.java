package jedi.assertion;

public interface AssertionDelegate {

	void assertTrue(boolean value, String message, Object... context);
}
