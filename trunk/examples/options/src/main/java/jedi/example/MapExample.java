package jedi.example;

import jedi.option.OptionMatcher;
import jedi.option.Options;

import java.util.HashMap;
import java.util.Map;

public class MapExample {

	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);

        // get a value from a map as as option
		Options.get(map, "a").match(new OptionMatcher<Integer>() {
		
			public void caseNone() {
				System.out.println("oops, shouldn't be here");
			}

			public void caseSome(Integer value) {
				System.out.println("matched " + value);
			}
		});

        // do it with a for
        for (Integer v : Options.get(map, "a")) System.out.println(v);
	}
}
