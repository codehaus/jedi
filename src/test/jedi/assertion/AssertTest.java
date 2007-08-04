package jedi.assertion;

import static jedi.functional.Coercions.list;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class AssertTest extends TestCase {

	private static final String[] AN_EMPTY_ARRAY = new String[0];
	private static final List<String> NON_EMPTY_COLLECTION = list("a");
	private static final String[] AN_ARRAY = new String[] { "a", "b", "c" };
	private static final Object[] CONTEXT = new Object[0];

	public void testAssertSameWhenSame() throws Exception {
		Assert.assertSame(this, this, "foo");
	}

	public void testAssertSameWhenDifferent() throws Exception {
		try {
			Assert.assertSame("a", "b", "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertEqualWhenSame() {
		Assert.assertEqual(this, this, "name");
	}

	public void testAssertEqualWhenEqual() {
		Assert.assertEqual("a", "a", "name");
	}

	public void testAssertEqualWhenNotEqual() {
		try {
			Assert.assertEqual("a", "b", "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertOneOfWhenValuesContainValue() {
		assertEquals("b", Assert.assertOneOf(AN_ARRAY, "b", "name"));
	}

	public void testAssertOneOfWhenValuesDoNotContainValue() {
		try {
			Assert.assertOneOf(AN_ARRAY, "c", "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertTrueWhenTrue() {
		Assert.assertTrue(true, "name", CONTEXT);
	}

	public void testAssertTrueWhenFalse() {
		try {
			Assert.assertTrue(false, "name", CONTEXT);
		} catch (AssertionError expected) {
		}
	}

	public void testAssertNullWhenNull() {
		Assert.assertNull(null, "name");
	}

	public void testAssertNullWhenNotNull() {
		try {
			Assert.assertNull("a", "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertStringNotNullOrEmpty() {
		String x = "a";
		assertSame(x, Assert.assertNotNullOrEmpty(x, "name"));
	}

	public void testAssertStringNotNullOrEmptyWhenNull() {
		String c = null;
		try {
			Assert.assertNotNullOrEmpty(c, "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertStringNotNullOrEmptyWhenEmpty() {
		try {
			Assert.assertNotNullOrEmpty("", "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertCollectionNotNullOrEmpty() {
		assertSame(NON_EMPTY_COLLECTION, Assert.assertNotNullOrEmpty(NON_EMPTY_COLLECTION, "name"));
	}

	public void testAssertCollectionNotNullOrEmptyWhenNull() {
		Collection<?> c = null;
		try {
			Assert.assertNotNullOrEmpty(c, "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertCollectionNotNullOrEmptyWhenEmpty() {
		try {
			Assert.assertNotNullOrEmpty(Collections.emptyList(), "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertArrayNotNullOrEmpty() {
		assertSame(AN_ARRAY, Assert.assertNotNullOrEmpty(AN_ARRAY, "name"));
	}

	public void testAssertArrayNotNullOrEmptyWhenNull() {
		Object[] c = null;
		try {
			Assert.assertNotNullOrEmpty(c, "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertArrayNotNullOrEmptyWhenEmpty() {
		try {
			Assert.assertNotNullOrEmpty(AN_EMPTY_ARRAY, "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertGreaterThanOrEqualToWhenEqual() {
		String a = "a";
		String b = "a";
		assertSame(b, Assert.assertGreaterThanOrEqualTo(a, b, "name"));
	}

	public void testAssertGreaterThanOrEqualToWhenGreaterThan() {
		String a = "a";
		String b = "b";
		assertSame(b, Assert.assertGreaterThanOrEqualTo(a, b, "name"));
	}

	public void testAssertGreaterThanOrEqualToWhenLessThan() {
		String a = "b";
		String b = "a";
		try {
			Assert.assertGreaterThanOrEqualTo(a, b, "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertLessThanOrEqualToWhenEqual() {
		String a = "a";
		String b = "a";
		assertSame(b, Assert.assertLessThanOrEqualTo(a, b, "name"));
	}

	public void testAssertLessThanOrEqualToWhenLessThan() {
		String a = "b";
		String b = "a";
		assertSame(b, Assert.assertLessThanOrEqualTo(a, b, "name"));
	}

	public void testAssertLessThanOrEqualToWhenGreaterThan() {
		String a = "a";
		String b = "b";
		try {
			Assert.assertLessThanOrEqualTo(a, b, "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertWithinClosedRangeoWhenLessThanLowerValue() {
		String b = "b";
		String d = "d";
		try {
			Assert.assertWithinClosedRange(b, d, "a", "name");
		} catch (AssertionError expected) {
		}
	}

	public void testAssertWithinClosedRangeoWhenEqualToLowerValue() {
		String b = "b";
		String d = "d";
		assertSame(b, Assert.assertWithinClosedRange(b, d, b, "name"));
	}

	public void testAssertWithinClosedRangeoWhenInRange() {
		String b = "b";
		String c = "c";
		String d = "d";
		assertSame(c, Assert.assertWithinClosedRange(b, d, c, "name"));
	}

	public void testAssertWithinClosedRangeoWhenEqualToUpperValue() {
		String b = "b";
		String d = "d";
		assertSame(d, Assert.assertWithinClosedRange(b, d, d, "name"));
	}

	public void testAssertWithinClosedRangeoWhenGreaterThanUpoerValue() {
		String b = "b";
		String d = "d";
		try {
			Assert.assertWithinClosedRange(b, d, "e", "name");
		} catch (AssertionError expected) {
		}
	}

	public void testFail() throws Exception {
		try {
			Assert.fail("fail", CONTEXT);
		} catch (AssertionError expected) {
		}
	}

	public void testAssertFalseWhenFalse() {
		Assert.assertFalse(false, "name");
	}

	public void testAssertFalseWhenTrue() {
		try {
			Assert.assertFalse(true, "name");
		} catch (AssertionError expected) {
		}
	}

}
