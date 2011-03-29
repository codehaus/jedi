package jedi.example;

import jedi.option.Option;
import jedi.option.OptionMatcher;

import static jedi.option.Options.some;

public class OptionExample {

	public static void main(String[] args) {
		Option<String> x = some("foo");

        // Matching using a visitor
		x.match(new OptionMatcher<String>() {
			public void caseNone() {
				System.out.println("oops, shouldn't be here");
			}

			public void caseSome(String value) {
				System.out.println("matched " + value);
			}
		});

        // match with for if you're only interested in Some
        for (String v : x) System.out.println("Got " + v);
	}
}
