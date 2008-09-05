package jedi.example;

import static jedi.option.Options.Some;
import jedi.functional.Functor;
import jedi.option.None;
import jedi.option.Option;
import jedi.option.OptionMatcher;

public class OptionExample {

	private static final class AddBar implements Functor<String, String> {
		public String execute(String value) {
			return value + "bar";
		}
	}

	public static void main(String[] args) {
		Option<String> x = Some("foo");

		System.out.println("Expect 'foo': " + x.get());
		System.out.println("Expect 'foobar': " + x.map(new AddBar()).get());

		x.match(new OptionMatcher<String>() {
			public void caseNone(None<String> none) {
				System.out.println("oops, shouldn't be here");
			}

			public void caseSome(String value) {
				System.out.println("matched " + value);
			}
		});
	}
}
