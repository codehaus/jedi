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

	@Test(expected=AssertionError.class)
	public void testAssertSameWhenDifferent() throws Exception {
		Assert.assertSame("a", "b", "name");
	}

	@Test
	public void testAssertEqualWhenSame() {
		Assert.assertEqual(this, this, "name");
	}

	@Test
	public void testAssertEqualWhenEqual() {
		Assert.assertEqual("a", new String("a"), "name");
	}

	@Test(expected=AssertionError.class)
	public void testAssertEqualWhenNotEqual() {
		Assert.assertEqual("a", "b", "name");
	}

	@Test
	public void testAssertOneOfWhenValuesContainValue() {
		junit.framework.Assert.assertEquals("b", Assert.assertOneOf(AN_ARRAY, "b", "name"));
	}

	@Test(expected=AssertionError.class)
	public void testAssertOneOfWhenValuesDoNotContainValue() {
		Assert.assertOneOf(AN_ARRAY, "d", "name");
	}

	@Test
	public void testAssertTrueWhenTrue() {
		Assert.assertTrue(true, "name", CONTEXT);
	}

	@Test(expected=AssertionError.class)
	public void testAssertTrueWhenFalse() {
		Assert.assertTrue(false, "name", CONTEXT);
	}

	@Test
	public void testAssertNullWhenNull() {
		Assert.assertNull(null, "name");
	}

	@Test(expected=AssertionError.class)
	public void testAssertNullWhenNotNull() {
		Assert.assertNull("a", "name");
	}

	@Test
	public void testAssertStringNotNullOrEmpty() {
		String x = "a";
		junit.framework.Assert.assertSame(x, Assert.assertNotNullOrEmpty(x, "name"));
	}

	@Test(expected=AssertionError.class)
	public void testAssertStringNotNullOrEmptyWhenNull() {
		String c = null;
		Assert.assertNotNullOrEmpty(c, "name");
	}

	@Test(expected=AssertionError.class)
	public void testAssertStringNotNullOrEmptyWhenEmpty() {
		Assert.assertNotNullOrEmpty("", "name");
	}

	@Test
	public void testAssertCollectionNotNullOrEmpty() {
		junit.framework.Assert.assertSame(NON_EMPTY_COLLECTION, Assert.assertNotNullOrEmpty(NON_EMPTY_COLLECTION, "name"));
	}

	@Test(expected=AssertionError.class)
	public void testAssertCollectionNotNullOrEmptyWhenNull() {
		Collection<?> c = null;
		Assert.assertNotNullOrEmpty(c, "name");
	}

	@Test(expected=AssertionError.class)
	public void testAssertCollectionNotNullOrEmptyWhenEmpty() {
		Assert.assertNotNullOrEmpty(Collections.emptyList(), "name");
	}

	@Test
	public void testAssertArrayNotNullOrEmpty() {
		junit.framework.Assert.assertSame(AN_ARRAY, Assert.assertNotNullOrEmpty(AN_ARRAY, "name"));
	}

	@Test(expected=AssertionError.class)
	public void testAssertArrayNotNullOrEmptyWhenNull() {
		Object[] c = null;
		Assert.assertNotNullOrEmpty(c, "name");
	}

	@Test(expected=AssertionError.class)
	public void testAssertArrayNotNullOrEmptyWhenEmpty() {
		Assert.assertNotNullOrEmpty(AN_EMPTY_ARRAY, "name");
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

	@Test(expected=AssertionError.class)
	public void testAssertGreaterThanOrEqualToWhenLessThan() {
		String a = "b";
		String b = "a";
		Assert.assertGreaterThanOrEqualTo(a, b, "name");
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

	@Test(expected=AssertionError.class)
	public void testAssertLessThanOrEqualToWhenGreaterThan() {
		String a = "a";
		String b = "b";
		Assert.assertLessThanOrEqualTo(a, b, "name");
	}

	@Test(expected=AssertionError.class)
	public void testAssertWithinClosedRangeoWhenLessThanLowerValue() {
		String b = "b";
		String d = "d";
		Assert.assertWithinClosedRange(b, d, "a", "name");
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

	@Test(expected=AssertionError.class)
	public void testAssertWithinClosedRangeoWhenGreaterThanUpoerValue() {
		String b = "b";
		String d = "d";
		Assert.assertWithinClosedRange(b, d, "e", "name");
	}

	@Test(expected=AssertionError.class)
	public void testFail() throws Exception {
		Assert.fail("fail", CONTEXT);
	}

	@Test
	public void testAssertFalseWhenFalse() {
		Assert.assertFalse(false, "name");
	}

	@Test(expected=AssertionError.class)
	public void testAssertFalseWhenTrue() {
		Assert.assertFalse(true, "name");
	}

}
