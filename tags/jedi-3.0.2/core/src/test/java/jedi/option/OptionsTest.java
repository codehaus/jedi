package jedi.option;

import static jedi.option.Options.get;
import static jedi.option.Options.none;
import static jedi.option.Options.option;
import static jedi.option.Options.some;
import static junit.framework.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

	@Test
	public void testMapGetSome() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);

		assertEquals(some(1), get(map, "a"));
	}

	@Test
	public void testMapGetNone() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);

		assertEquals(none(), get(map, "b"));
	}

	@Test
	public void testMapGetNoneFromNull() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", null);
		assertEquals(none(), get(map, "a"));
	}

	@Test
	public void testOptionWithNull() {
		assertEquals(none(), option(null));
	}

	@Test
	public void testOptionWithSomething() {
		assertEquals(some("Something"), option("Something"));
	}

}
