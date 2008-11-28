package jedi.option;

import static jedi.option.Options.none;
import static jedi.option.Options.some;

import java.util.Arrays;
import java.util.Iterator;

import jedi.functional.Command;
import jedi.functional.Command0;
import jedi.functional.Filter;
import jedi.functional.Functor;
import jedi.functional.Functor0;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.junit.Test;

public class SomeTest extends MockObjectTestCase {

	@Test
	public void testAsList() {
		assertEquals(Arrays.asList("hi"), some("hi").asList());
	}

	@Test
	public void testEqualsWhenEqual() {
		assertEquals(some("a"), some("a"));
		assertEquals(some("a").hashCode(), some("a").hashCode());
	}

	@Test
	public void testEqualsWhenNotEqual() {
		assertFalse(some("a").equals("b"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFilterWhenFilterFails() {
		Option<String> option = some("hi");
		Mock f = mock(Filter.class);
		f.expects(once()).method("execute").with(eq("hi")).will(returnValue(false));
		assertEquals(none(), option.filter((Filter<String>) f.proxy()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFilterWhenFilterPasses() {
		Option<String> option = some("hi");
		Mock f = mock(Filter.class);
		f.expects(once()).method("execute").with(eq("hi")).will(returnValue(true));
		assertSame(option, option.filter((Filter<String>) f.proxy()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testForEach() {
		Mock command = mock(Command.class);
		command.expects(once()).method("execute").with(eq("x"));
		some("x").forEach((Command<String>) command.proxy());
	}

	public void testForEachWithSuperclassCommand() {
		some(Bar.BAR).forEach(new Command<Foo>() {
			public void execute(Foo value) {
				assertSame(Bar.BAR, value);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetOrElse() {
		Mock generator = mock(Functor0.class);
		generator.expects(never()).method("execute");
		assertTrue(some(true).getOrElse((Functor0<Boolean>) generator.proxy()));
	}

	@Test
	public void testIterator() {
		Option<String> option = some("hi");
		Iterator<String> iterator = option.iterator();
		assertTrue(iterator.hasNext());
		assertEquals("hi", iterator.next());
		assertFalse(iterator.hasNext());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMap() {
		Mock functor = mock(Functor.class);
		functor.expects(once()).method("execute").with(eq("string")).will(returnValue(true));
		assertEquals(some(true), some("string").map((Functor<String, Boolean>) functor.proxy()));
	}

	public void testMapWithSuperCommand() {
		assertEquals(some(Boolean.TRUE), some(Bar.BAR).map(new Functor<Foo, Boolean>() {
			public Boolean execute(Foo value) {
				return Boolean.TRUE;
			}
		}));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMatchWithCommands() {
		Option<String> opt = some("x");

		Mock someCommand = mock(Command.class);
		Mock noneCommand = mock(Command0.class);
		someCommand.expects(once()).method("execute").with(eq("x"));

		opt.match((Command) someCommand.proxy(), (Command0) noneCommand.proxy());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMatchWithFunctors() {
		Option<String> option = some("hi");
		Mock someFunctor = mock(Functor.class);
		Mock noneFunctor = mock(Functor0.class);
		someFunctor.expects(once()).method("execute").with(eq("hi")).will(returnValue(new Integer(1)));
		assertEquals(new Integer(1), option.match((Functor<String, Integer>) someFunctor.proxy(), (Functor0<Integer>) noneFunctor.proxy()));
	}

	@Test
	public void testMatchWithSuperclassOptionMatcher() {
		Option<Bar> opt = some(new Bar());

		opt.match(new OptionMatcher<Foo>() {
			public void caseNone() {
				fail();
			}

			public void caseSome(Foo value) {
				// don't care
			}
		});
	}

	@Test
	public void testMatchWithSuperCommand() {
		Option<Bar> opt = some(Bar.BAR);
		Mock noneCommand = mock(Command0.class);
		opt.match(new Command<Foo>() {
			public void execute(Foo value) {
				assertSame(Bar.BAR, value);
			}
		}, (Command0) noneCommand.proxy());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMatchWithSuperFunctors() {
		Option<Bar> option = some(Bar.BAR);
		Mock noneFunctor = mock(Functor0.class);
		assertEquals(new Integer(1), option.match(new Functor<Foo, Integer>() {
			public Integer execute(Foo value) {
				return 1;
			}
		}, (Functor0<Integer>) noneFunctor.proxy()));
	}
}
