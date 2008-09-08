package jedi.option;

import static jedi.option.Options.None;
import jedi.functional.Command;
import jedi.functional.Command0;
import jedi.functional.Filter;
import jedi.functional.Functor;
import jedi.functional.Functor0;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class NoneTest extends MockObjectTestCase {

	public void testMatch() {
		Option<Integer> opt = None();

		opt.match(new OptionMatcher<Integer>() {
			public void caseNone(None<Integer> none) {
			}

			public void caseSome(Integer value) {
				fail();
			}
		});
	}
	@SuppressWarnings("unchecked")
	public void testMatchWithCommands() {
		Option<String> opt = None();

		Mock someCommand = mock(Command.class);
		Mock noneCommand = mock(Command0.class);
		noneCommand.expects(once()).method("execute");
		
		opt.match((Command)someCommand.proxy(), (Command0)noneCommand.proxy());
	}
	
	@SuppressWarnings("unchecked")
	public void testMatchWithFunctors() {
		Option<String> option = None();
		Mock someFunctor = mock(Functor.class);
		Mock noneFunctor = mock(Functor0.class);
		noneFunctor.expects(once()).method("execute").will(returnValue(new Integer(100)));
		assertEquals(new Integer(100), option.match((Functor<String, Integer>) someFunctor.proxy(), (Functor0<Integer>) noneFunctor.proxy()));
	}
	
	public void testAsList() {
		assertTrue(None().asList().isEmpty());
	}
	
	@SuppressWarnings("unchecked")
	public void testGetOrElse() {
		Mock generator = mock(Functor0.class);
		generator.expects(once()).method("execute").will(returnValue("x"));
		assertEquals("x", Options.<String>None().getOrElse((Functor0<String>) generator.proxy()));
	}
	
	@SuppressWarnings("unchecked")
	public void testMap() {
		Mock functor = mock(Functor.class);
		functor.expects(never()).method("execute");
		assertEquals(Options.<Boolean>None(), Options.<String>None().map((Functor<String, Boolean>) functor.proxy()));
	}
	
	@SuppressWarnings("unchecked")
	public void testForEach() {
		Mock command = mock(Command.class);
		command.expects(never()).method("execute");
		Options.None().forEach((Command<Object>) command.proxy());
	}
	
	public void testEqualsWhenEqual() {
		assertEquals(None(), None());
		assertEquals(None().hashCode(), None().hashCode());
	}
	
	public void testEqualsWhenNotEqual() {
		assertFalse(None().equals("s"));
	}
	
	@SuppressWarnings("unchecked")
	public void testFilter() {
		Mock f = mock(Filter.class);
		assertEquals(None(), None().filter((Filter<Object>) f.proxy()));
	}
}
