package jedi.example;

import static jedi.option.Options.Some;
import jedi.option.None;
import jedi.option.Option;
import jedi.option.OptionMatcher;

public class OptionExample {

	public static void main(String[] args) {
		Option<String> x = Some("foo");
		
		x.match(new OptionMatcher<String>() {
			public void caseNone() {
				System.out.println("oops, shouldn't be here");
			}

			public void caseSome(String value) {
				System.out.println("matched " + value);
			}
		});
	}
}
