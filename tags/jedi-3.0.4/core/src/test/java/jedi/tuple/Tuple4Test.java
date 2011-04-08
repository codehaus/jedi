package jedi.tuple;

import static jedi.tuple.Tuples.tuple3;
import static jedi.tuple.Tuples.tuple4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class Tuple4Test {
	@Test
	public void testFactory() {
		Tuple4<String, Integer, Character, Boolean> tuple = tuple4("a", 1, 'c', true);
		assertEquals("a", tuple.a());
		assertEquals(Integer.valueOf(1), tuple.b());
		assertEquals(Character.valueOf('c'), tuple.c());
		assertEquals(Boolean.TRUE, tuple.d());
	}

	@Test
	public void testEquals() {
		assertEquals(tuple4(null, null, null, null), tuple4(null, null, null, null));
		assertEquals(tuple4("a", null, null, null), tuple4("a", null, null, null));
		assertEquals(tuple4(null, "a", null, null), tuple4(null, "a", null, null));
		assertEquals(tuple4(null, null, "a", null), tuple4(null, null, "a", null));
		assertEquals(tuple4(null, null, null, "a"), tuple4(null, null, null, "a"));
		assertEquals(tuple4(1,2,3,4), tuple4(1,2,3,4));

		assertFalse(tuple4(null, null, null, null).equals(tuple4("a", null, null, null)));
		assertFalse(tuple4(null, null, null, null).equals(tuple4(null, "a", null, null)));
		assertFalse(tuple4(null, null, null, null).equals(tuple4(null, null, "a", null)));
		assertFalse(tuple4(null, null, null, null).equals(tuple4(null, null, null, "a")));
	}

	@Test
	public void testWithOptionsEquals() {
		assertEquals(tuple4(null, null, null, null).withOptions(), tuple4(null, null, null, null).withOptions());
		assertEquals(tuple4("a", null, null, null).withOptions(), tuple4("a", null, null, null).withOptions());
		assertEquals(tuple4(null, "a", null, null).withOptions(), tuple4(null, "a", null, null).withOptions());
		assertEquals(tuple4(null, null, "a", null).withOptions(), tuple4(null, null, "a", null).withOptions());
		assertEquals(tuple4(null, null, null, "a").withOptions(), tuple4(null, null, null, "a").withOptions());
		assertEquals(tuple4(1,2,3,4).withOptions(), tuple4(1,2,3,4).withOptions());

		assertFalse(tuple4(null, null, null, null).withOptions().equals(tuple4("a", null, null, null).withOptions()));
		assertFalse(tuple4(null, null, null, null).withOptions().equals(tuple4(null, "a", null, null).withOptions()));
		assertFalse(tuple4(null, null, null, null).withOptions().equals(tuple4(null, null, "a", null).withOptions()));
		assertFalse(tuple4(null, null, null, null).withOptions().equals(tuple4(null, null, null, "a").withOptions()));
	}
}
