package jedi.option;

import jedi.functional.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * None represents a non existent value.
 */
public final class None<T> implements Option<T> {

    public static final long serialVersionUID = 1L;

	private final EmptyIterator<T> iterator = new EmptyIterator<T>();

	public List<T> asList() {
		return Collections.<T> emptyList();
	}

	public T getOrElse(Functor0<? extends T> generator) {
		return generator.execute();
	}

	public T getOrElse(T defaultValue) {
		return defaultValue;
	}

	public void match(OptionMatcher<? super T> matcher) {
		matcher.caseNone();
	}

	public void match(Command<? super T> someCommand, Command0 noneCommand) {
		noneCommand.execute();
	}

	public <R1, R2 extends R1> R1 match(Functor<? super T, R1> someFunctor, Functor0<R2> noneFunctor) {
		return noneFunctor.execute();
	}

	public <R> Option<R> map(Functor<? super T, R> mappingFunction) {
		return Options.<R> none();
	}

	public <R> Option<R> flatMap(Functor<? super T, Option<R>> mappingFunction) {
		return Options.<R> none();
	}

	@SuppressWarnings("unchecked")
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

	/**
	 * @return null
	 */
	public T unsafeGet() {
		return null;
	}

}
