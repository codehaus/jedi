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

	public None() {
	}

	public List<T> asList() {
		return Collections.<T> emptyList();
	}

	public T getOrElse(Functor0<T> generator) {
		return generator.execute();
	}
	
	public T getOrElse(T defaultValue) {
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public void match(OptionMatcher matcher) {
		matcher.caseNone(this);
	}

	public void match(Command<T> someCommand, Command0 noneCommand) {
		noneCommand.execute();
	}
	
	public <R> R match(Functor<T, R> someFunctor, Functor0<R> noneFunctor) {
		return noneFunctor.execute();
	}
	
	public <R> Option<R> map(Functor<T, R> mappingFunction) {
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

	public void forEach(Command<T> command) {
		// no-op
	}

	public Option<T> filter(Filter<T> f) {
		return this;
	}

	public Iterator<T> iterator() {
		return iterator;
	}

}
