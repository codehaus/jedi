package jedi.option;

import static jedi.option.Options.none;

import java.util.Iterator;

import jedi.functional.Command;
import jedi.functional.Command0;
import jedi.functional.Filter;
import jedi.functional.Functor;
import jedi.functional.Functor0;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.junit.Test;

public class NoneTest extends MockObjectTestCase {

	@Test
	public void testMatch() {
		Option<Integer> opt = none();

		opt.match(new OptionMatcher<Integer>() {
			public void caseNone(None<Integer> none) {
			}

			public void caseSome(Integer value) {
				fail();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMatchWithCommands() {
		Option<String> opt = none();

		Mock someCommand = mock(Command.class);
		Mock noneCommand = mock(Command0.class);
		noneCommand.expects(once()).method("execute");

		opt.match((Command) someCommand.proxy(), (Command0) noneCommand.proxy());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMatchWithFunctors() {
		Option<String> option = none();
		Mock someFunctor = mock(Functor.class);
		Mock noneFunctor = mock(Functor0.class);
		noneFunctor.expects(once()).method("execute").will(returnValue(new Integer(100)));
		assertEquals(new Integer(100), option
				.match((Functor<String, Integer>) someFunctor.proxy(), (Functor0<Integer>) noneFunctor.proxy()));
	}

	@Test
	public void testAsList() {
		assertTrue(none().asList().isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetOrElse() {
		Mock generator = mock(Functor0.class);
		generator.expects(once()).method("execute").will(returnValue("x"));
		assertEquals("x", Options.<String> none().getOrElse((Functor0<String>) generator.proxy()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMap() {
		Mock functor = mock(Functor.class);
		functor.expects(never()).method("execute");
		assertEquals(Options.<Boolean> none(), Options.<String> none().map((Functor<String, Boolean>) functor.proxy()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testForEach() {
		Mock command = mock(Command.class);
		command.expects(never()).method("execute");
		Options.none().forEach((Command<Object>) command.proxy());
	}

	@Test
	public void testEqualsWhenEqual() {
		assertEquals(none(), none());
		assertEquals(none().hashCode(), none().hashCode());
	}

	@Test
	public void testEqualsWhenNotEqual() {
		assertFalse(none().equals("s"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFilter() {
		Mock f = mock(Filter.class);
		assertEquals(none(), none().filter((Filter<Object>) f.proxy()));
	}

	@Test
	public void testIterator() {
		Option<String> option = none();
		Iterator<String> iterator = option.iterator();
		assertFalse(iterator.hasNext());
	}
}
