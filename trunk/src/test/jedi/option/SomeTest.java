package jedi.option;

import static jedi.option.Options.Some;

import java.util.Arrays;

import jedi.functional.Functor;
import jedi.functional.Generator;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class SomeTest extends MockObjectTestCase {

	public void testMatch() {
		Option<Integer> opt = Some(new Integer(1));
		
		opt.match(new OptionMatcher<Integer>() {
			public void caseNone(None<Integer> none) {
				fail();
			}

			public void caseSome(Integer value) {
			}
		});
	}
	
	public void testIsEmpty() {
		assertFalse(Some("a").isEmpty());
	}
	
	public void testAsList() {
		assertEquals(Arrays.asList("hi"), Some("hi").asList());
	}
	
	@SuppressWarnings("unchecked")
	public void testGetOrElse() {
		Mock generator = mock(Generator.class);
		generator.expects(never()).method("execute");
		assertTrue(Some(true).getOrElse((Generator<Boolean>) generator.proxy()));
	}

	@SuppressWarnings("unchecked")
	public void testMap() {
		Mock functor = mock(Functor.class);
		functor.expects(once()).method("execute").with(eq("string")).will(returnValue(true));
		assertEquals(Some(true),Some("string").map((Functor<String, Boolean>) functor.proxy()));
	}
}
