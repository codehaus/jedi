package jedi.tuple;

import static jedi.tuple.Tuples.tuple3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class Tuple3Test {
	@Test
	public void testFactory() {
		Tuple3<String, Integer, Character> tuple = tuple3("a", 1, 'c');
		assertEquals("a", tuple.a());
		assertEquals(Integer.valueOf(1), tuple.b());
		assertEquals(Character.valueOf('c'), tuple.c());
	}

	@Test
	public void testEquals() {
		assertEquals(tuple3(null, null, null), tuple3(null, null, null));
		assertEquals(tuple3("a", null, null), tuple3("a", null, null));
		assertEquals(tuple3(null, "a", null), tuple3(null, "a", null));
		assertEquals(tuple3(null, null, "a"), tuple3(null, null, "a"));
		assertEquals(tuple3(1,2,3), tuple3(1,2,3));

		assertFalse(tuple3(null, null, null).equals(tuple3("a", null, null)));
		assertFalse(tuple3(null, null, null).equals(tuple3(null, "a", null)));
		assertFalse(tuple3(null, null, null).equals(tuple3(null, null, "a")));
	}

	@Test
	public void testWithOptionsEquals() {
		assertEquals(tuple3(null, null, null).withOptions(), tuple3(null, null, null).withOptions());
		assertEquals(tuple3("a", null, null).withOptions(), tuple3("a", null, null).withOptions());
		assertEquals(tuple3(null, "a", null).withOptions(), tuple3(null, "a", null).withOptions());
		assertEquals(tuple3(null, null, "a").withOptions(), tuple3(null, null, "a").withOptions());
		assertEquals(tuple3(1,2,3).withOptions(), tuple3(1,2,3).withOptions());

		assertFalse(tuple3(null, null, null).withOptions().equals(tuple3("a", null, null).withOptions()));
		assertFalse(tuple3(null, null, null).withOptions().equals(tuple3(null, "a", null).withOptions()));
		assertFalse(tuple3(null, null, null).withOptions().equals(tuple3(null, null, "a").withOptions()));
	}
}