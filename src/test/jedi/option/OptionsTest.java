package jedi.option;

import static jedi.option.Options.None;
import static jedi.option.Options.Some;
import static jedi.option.Options.get;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class OptionsTest extends TestCase {

	public void testMapGetSome() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);
		
		assertEquals(Some(1), get(map, "a"));
	}

	public void testMapGetNone() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);
		
		assertEquals(None(), get(map, "b"));
	}
	
}
