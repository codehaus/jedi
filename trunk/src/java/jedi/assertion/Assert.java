package jedi.assertion;

import static jedi.functional.Coercions.*;

import java.util.Collection;

public class Assert {

	private static AssertionDelegate	delegate	= new ErrorThrowingAssertionDelegate();

	public static void setDelegate(AssertionDelegate delegate) {
		Assert.delegate = delegate;
	}

	protected Assert() {
	}

    public static void assertSame(Object value1, Object value2, String assertionName) {
        assertTrue(value1 == value2, assertionName, value1, value2);
    }

	public static void assertEqual(Object value1, Object value2, String assertionName) {
		assertNotNull(value1, assertionName + ".value1");
		assertNotNull(value2, assertionName + ".value2");

		assertTrue(value1.equals(value2), assertionName, value1, value2);
	}

	public static void assertOneOf(Object[] values, Object value, String assertionName) {
		assertNotNull(values, assertionName + ".values");
		assertNotNull(value, assertionName + ".value");

		Collection<Object> valuesAsList = asList(values);
		assertTrue(valuesAsList.contains(value), assertionName, valuesAsList, value);
	}

	public static void assertTrue(boolean value, String assertionName, Object... context) {
		delegate.assertTrue(value, assertionName, context);
	}

	public static void assertNull(Object obj, String assertionName) {
		assertTrue(obj == null, assertionName, obj);
	}

	public static void assertNotNull(Object obj, String assertionName, Object... context) {
		assertTrue(obj != null, assertionName, context);
	}

	public static void assertNotNullOrEmpty(String string, String assertionName) {
		assertTrue(null != string && string.length() > 0, assertionName);
	}

	public static <T> void assertNotNullOrEmpty(Collection<? extends T> collection, String assertionName) {
		assertTrue(null != collection && collection.size() > 0, assertionName);
	}

    public static void assertNotNullOrEmpty(Object[] array, String assertionName) {
        assertTrue(null != array && array.length > 0, assertionName);
    }

    public static <T extends Comparable<? super T>> void assertGreaterThanOrEqualTo(T min, T value, String assertionName) {
		assertTrue(value.compareTo(min) >= 0, assertionName, min, value);
	}

	public static <T extends Comparable<? super T>> void assertLessThanOrEqualTo(T max, T value, String assertionName) {
		assertTrue(value.compareTo(max) <= 0, assertionName, max, value);
	}

	public static <T extends Comparable<? super T>> void assertWithinClosedRange(T min, T max, T value, String assertionName) {
		assertGreaterThanOrEqualTo(min, value, assertionName);
		assertLessThanOrEqualTo(max, value, assertionName);
	}

	public static Object fail(String assertionName, Object... context) {
		assertTrue(false, assertionName, context);
		return null; // to prevent spurious return statements
	}

	public static void assertFalse(boolean value, String assertionName) {
		assertTrue(!value, assertionName);
	}
}
