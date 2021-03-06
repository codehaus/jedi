package jedi.assertion;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class ErrorThrowingAssertionDelegateTest {

	private static final class DodgyToString {
		@Override
		public String toString() {
			throw new RuntimeException("Dodgy Error");
		}
	}

	private static final Object[] CONTEXT = new String[] { "a", "b" };

	private ErrorThrowingAssertionDelegate delegate = new ErrorThrowingAssertionDelegate();

	@Test
	public void testAssertTrueWithTrueDoesNotThrowAnAssertionException() {
		delegate.assertTrue(true, "message", CONTEXT);
	}

	@Test
	public void testAssertTrueWithFalseAndContextWithItems() {
		executeErrorTest("message: context {[a], [b]}", CONTEXT);
	}

	@Test
	public void testAssertTrueWithFalseAndNullContext() {
		executeErrorTest("message", (Object[]) null);
	}

	@Test
	public void testAssertTrueWithFalseAndEmptyContext() {
		executeErrorTest("message", new Object[0]);
	}

	@Test
	public void testAssertTrueWithFalseAndContextItemThrowsExceptionInToString() {
		try {
			delegate.assertTrue(false, "message", (new Object[] { new DodgyToString() }));
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
