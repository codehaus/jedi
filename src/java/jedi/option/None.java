package jedi.option;

import java.util.Collections;
import java.util.List;

import jedi.functional.Command;
import jedi.functional.Filter;
import jedi.functional.Functor;
import jedi.functional.Generator;

/**
 * None represents a non existent value.
 */
public final class None<T> implements Option<T> {

	public None() {
	}

	public List<T> asList() {
		return Collections.<T> emptyList();
	}

	public T getOrElse(Generator<T> generator) {
		return generator.execute();
	}
	
	public T getOrElse(T defaultValue) {
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public void match(OptionMatcher matcher) {
		matcher.caseNone(this);
	}

	public <R> Option<R> map(Functor<T, R> mappingFunction) {
		return Options.<R> None();
	}

	public void match(Command<T> someCommand, Command<None<T>> noneCommand) {
		noneCommand.execute(new None<T>());
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

}
