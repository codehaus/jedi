package jedi.example;

import java.util.HashMap;
import java.util.Map;

import jedi.option.OptionMatcher;
import jedi.option.Options;

public class MapExample {

	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);
		
		Options.get(map, "a").match(new OptionMatcher<Integer>() {
		
			public void caseNone() {
				System.out.println("oops, shouldn't be here");
			}

			public void caseSome(Integer value) {
				System.out.println("matched " + value);
			}
		});
	}
}
