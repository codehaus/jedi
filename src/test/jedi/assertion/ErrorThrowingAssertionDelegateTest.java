package jedi.assertion;

import junit.framework.TestCase;

public class ErrorThrowingAssertionDelegateTest extends TestCase {

	private static final class DodgyToString {
		@Override
		public String toString() {
			throw new RuntimeException("Dodgy Error");
		}
	}

	private static final Object[] CONTEXT = new String[] {"a","b"};

	private ErrorThrowingAssertionDelegate delegate = new ErrorThrowingAssertionDelegate();

	public void testAssertTrueWithTrueDoesNotThrowAnAssertionException() {
		delegate.assertTrue(true, "message", CONTEXT);
	}

	public void testAssertTrueWithFalseAndContextWithItems() {
		executeErrorTest("message: context {[a], [b]}", CONTEXT);
	}

	public void testAssertTrueWithFalseAndNullContext() {
		executeErrorTest("message", (Object[]) null);
	}

	public void testAssertTrueWithFalseAndEmptyContext() {
		executeErrorTest("message", new Object[0]);
	}

	public void testAssertTrueWithFalseAndContextItemThrowsExceptionInToString() {
		try {
			delegate.assertTrue(false, "message", (new Object[] {new DodgyToString()}));
		} catch (AssertionError expected) {
			assertTrue(expected.getMessage().contains("Dodgy Error"));
		}
	}

	private void executeErrorTest(String expectedMessage, Object[] context) {
		try {
			delegate.assertTrue(false, "message", context);
		} catch (AssertionError expected) {
			assertEquals(expectedMessage, expected.getMessage());
		}
	}

}
