package jedi.assertion;

import static jedi.functional.Coercions.list;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AssertTest {

	private static final String[] AN_EMPTY_ARRAY = new String[0];
	private static final List<String> NON_EMPTY_COLLECTION = list("a");
	private static final String[] AN_ARRAY = new String[] { "a", "b", "c" };
	private static final Object[] CONTEXT = new Object[0];

	@Test
	public void testAssertSameWhenSame() throws Exception {
		Assert.assertSame(this, this, "foo");
	}

	@Test
	public void testAssertSameWhenDifferent() throws Exception {
		try {
			Assert.assertSame("a", "b", "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertEqualWhenSame() {
		Assert.assertEqual(this, this, "name");
	}

	@Test
	public void testAssertEqualWhenEqual() {
		Assert.assertEqual("a", "a", "name");
	}

	@Test
	public void testAssertEqualWhenNotEqual() {
		try {
			Assert.assertEqual("a", "b", "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertOneOfWhenValuesContainValue() {
		junit.framework.Assert.assertEquals("b", Assert.assertOneOf(AN_ARRAY, "b", "name"));
	}

	@Test
	public void testAssertOneOfWhenValuesDoNotContainValue() {
		try {
			Assert.assertOneOf(AN_ARRAY, "c", "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertTrueWhenTrue() {
		Assert.assertTrue(true, "name", CONTEXT);
	}

	@Test
	public void testAssertTrueWhenFalse() {
		try {
			Assert.assertTrue(false, "name", CONTEXT);
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertNullWhenNull() {
		Assert.assertNull(null, "name");
	}

	@Test
	public void testAssertNullWhenNotNull() {
		try {
			Assert.assertNull("a", "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertStringNotNullOrEmpty() {
		String x = "a";
		junit.framework.Assert.assertSame(x, Assert.assertNotNullOrEmpty(x, "name"));
	}

	@Test
	public void testAssertStringNotNullOrEmptyWhenNull() {
		String c = null;
		try {
			Assert.assertNotNullOrEmpty(c, "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertStringNotNullOrEmptyWhenEmpty() {
		try {
			Assert.assertNotNullOrEmpty("", "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertCollectionNotNullOrEmpty() {
		junit.framework.Assert.assertSame(NON_EMPTY_COLLECTION, Assert.assertNotNullOrEmpty(NON_EMPTY_COLLECTION, "name"));
	}

	@Test
	public void testAssertCollectionNotNullOrEmptyWhenNull() {
		Collection<?> c = null;
		try {
			Assert.assertNotNullOrEmpty(c, "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertCollectionNotNullOrEmptyWhenEmpty() {
		try {
			Assert.assertNotNullOrEmpty(Collections.emptyList(), "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertArrayNotNullOrEmpty() {
		junit.framework.Assert.assertSame(AN_ARRAY, Assert.assertNotNullOrEmpty(AN_ARRAY, "name"));
	}

	@Test
	public void testAssertArrayNotNullOrEmptyWhenNull() {
		Object[] c = null;
		try {
			Assert.assertNotNullOrEmpty(c, "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertArrayNotNullOrEmptyWhenEmpty() {
		try {
			Assert.assertNotNullOrEmpty(AN_EMPTY_ARRAY, "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertGreaterThanOrEqualToWhenEqual() {
		String a = "a";
		String b = "a";
		junit.framework.Assert.assertSame(b, Assert.assertGreaterThanOrEqualTo(a, b, "name"));
	}

	@Test
	public void testAssertGreaterThanOrEqualToWhenGreaterThan() {
		String a = "a";
		String b = "b";
		junit.framework.Assert.assertSame(b, Assert.assertGreaterThanOrEqualTo(a, b, "name"));
	}

	@Test
	public void testAssertGreaterThanOrEqualToWhenLessThan() {
		String a = "b";
		String b = "a";
		try {
			Assert.assertGreaterThanOrEqualTo(a, b, "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertLessThanOrEqualToWhenEqual() {
		String a = "a";
		String b = "a";
		junit.framework.Assert.assertSame(b, Assert.assertLessThanOrEqualTo(a, b, "name"));
	}

	@Test
	public void testAssertLessThanOrEqualToWhenLessThan() {
		String a = "b";
		String b = "a";
		junit.framework.Assert.assertSame(b, Assert.assertLessThanOrEqualTo(a, b, "name"));
	}

	@Test
	public void testAssertLessThanOrEqualToWhenGreaterThan() {
		String a = "a";
		String b = "b";
		try {
			Assert.assertLessThanOrEqualTo(a, b, "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertWithinClosedRangeoWhenLessThanLowerValue() {
		String b = "b";
		String d = "d";
		try {
			Assert.assertWithinClosedRange(b, d, "a", "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertWithinClosedRangeoWhenEqualToLowerValue() {
		String b = "b";
		String d = "d";
		junit.framework.Assert.assertSame(b, Assert.assertWithinClosedRange(b, d, b, "name"));
	}

	@Test
	public void testAssertWithinClosedRangeoWhenInRange() {
		String b = "b";
		String c = "c";
		String d = "d";
		junit.framework.Assert.assertSame(c, Assert.assertWithinClosedRange(b, d, c, "name"));
	}

	@Test
	public void testAssertWithinClosedRangeoWhenEqualToUpperValue() {
		String b = "b";
		String d = "d";
		junit.framework.Assert.assertSame(d, Assert.assertWithinClosedRange(b, d, d, "name"));
	}

	@Test
	public void testAssertWithinClosedRangeoWhenGreaterThanUpoerValue() {
		String b = "b";
		String d = "d";
		try {
			Assert.assertWithinClosedRange(b, d, "e", "name");
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testFail() throws Exception {
		try {
			Assert.fail("fail", CONTEXT);
		} catch (AssertionError expected) {
		}
	}

	@Test
	public void testAssertFalseWhenFalse() {
		Assert.assertFalse(false, "name");
	}

	@Test
	public void testAssertFalseWhenTrue() {
		try {
			Assert.assertFalse(true, "name");
		} catch (AssertionError expected) {
		}
	}

}
