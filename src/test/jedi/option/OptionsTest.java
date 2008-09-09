package jedi.option;

import static jedi.option.Options.none;
import static jedi.option.Options.some;
import static jedi.option.Options.get;
import static jedi.option.Options.option;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class OptionsTest extends TestCase {

	public void testMapGetSome() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);
		
		assertEquals(some(1), get(map, "a"));
	}

	public void testMapGetNone() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);
		
		assertEquals(none(), get(map, "b"));
	}
	
	public void testOptionWithNull() {
		assertEquals(none(), option(null));
	}

	public void testOptionWithSomething() {
		assertEquals(some("Something"), option("Something"));
	}
	
}
