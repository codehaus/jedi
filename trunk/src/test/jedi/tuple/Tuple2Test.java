package jedi.tuple;

import static jedi.tuple.Tuples.tuple2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class Tuple2Test {

	@Test
	public void testEquals() {
		assertEquals(tuple2(null,null), tuple2(null,null));
		assertEquals(tuple2(null,"a"), tuple2(null,"a"));
		assertEquals(tuple2("a", null), tuple2("a", null));
		assertEquals(tuple2(1,2), tuple2(1,2));

		assertFalse(tuple2(null, null).equals(tuple2("a", null)));
	}

}
