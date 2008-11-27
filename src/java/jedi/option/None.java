package jedi.option;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import jedi.functional.Command;
import jedi.functional.Command0;
import jedi.functional.Filter;
import jedi.functional.Functor;
import jedi.functional.Functor0;

/**
 * None represents a non existent value.
 */
public final class None<T> implements Option<T> {

	private final class EmptyIterator implements Iterator<T> {
		public boolean hasNext() {
			return false;
		}

		public T next() {
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private final EmptyIterator iterator = new EmptyIterator();

	public List<T> asList() {
		return Collections.<T> emptyList();
	}

	public T getOrElse(Functor0<? extends T> generator) {
		return generator.execute();
	}

	public <E extends T> T getOrElse(E defaultValue) {
		return defaultValue;
	}

	public void match(OptionMatcher<? super T> matcher) {
		matcher.caseNone();
	}

	public void match(Command<? super T> someCommand, Command0 noneCommand) {
		noneCommand.execute();
	}

	public <R> R match(Functor<? super T, R> someFunctor, Functor0<R> noneFunctor) {
		return noneFunctor.execute();
	}

	public <R> Option<R> map(Functor<? super T, R> mappingFunction) {
		return Options.<R> none();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof None;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "None";
	}

	public Iterator<T> iterator() {
		return iterator;
	}

	public Option<T> filter(Filter<? super T> f) {
		return this;
	}

	public void forEach(Command<? super T> command) {
		// no op
	}

}
