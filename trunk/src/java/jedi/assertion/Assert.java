package jedi.assertion;

import static jedi.functional.Coercions.asList;

import java.util.Collection;

public class Assert {

	private static AssertionDelegate delegate = new ErrorThrowingAssertionDelegate();

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

	public static Object assertOneOf(Object[] values, Object value, String assertionName) {
		assertNotNull(values, assertionName + ".values");
		assertNotNull(value, assertionName + ".value");

		Collection<Object> valuesAsList = asList(values);
		assertTrue(valuesAsList.contains(value), assertionName, valuesAsList, value);
        return value;
    }

	public static void assertTrue(boolean value, String assertionName, Object... context) {
		delegate.assertTrue(value, assertionName, context);
	}

	public static <T> T assertNull(T obj, String assertionName) {
		assertTrue(obj == null, assertionName, obj);
        return obj;
    }

	public static <T> T assertNotNull(T obj, String assertionName, Object... context) {
		assertTrue(obj != null, assertionName, context);
        return obj;
    }

	public static String assertNotNullOrEmpty(String string, String assertionName) {
		assertTrue(null != string && string.length() > 0, assertionName);
        return string;
    }

	public static <T> Collection<? extends T> assertNotNullOrEmpty(Collection<? extends T> collection, String assertionName) {
		assertTrue(null != collection && collection.size() > 0, assertionName);
        return collection;
    }

    public static Object[] assertNotNullOrEmpty(Object[] array, String assertionName) {
        assertTrue(null != array && array.length > 0, assertionName);
        return array;
    }

    public static <T extends Comparable<? super T>> T assertGreaterThanOrEqualTo(T min, T value, String assertionName) {
		assertTrue(value.compareTo(min) >= 0, assertionName, min, value);
        return value;
    }

	public static <T extends Comparable<? super T>> T assertLessThanOrEqualTo(T max, T value, String assertionName) {
		assertTrue(value.compareTo(max) <= 0, assertionName, max, value);
        return value;
    }

	public static <T extends Comparable<? super T>> T assertWithinClosedRange(T min, T max, T value, String assertionName) {
		assertGreaterThanOrEqualTo(min, value, assertionName);
		assertLessThanOrEqualTo(max, value, assertionName);
        return value;
    }

	public static Object fail(String assertionName, Object... context) {
		assertTrue(false, assertionName, context);
		return null; // to prevent spurious return statements
	}

	public static void assertFalse(boolean value, String assertionName) {
		assertTrue(!value, assertionName);
	}
}
