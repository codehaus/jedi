package jedi.option;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

import jedi.functional.Command;
import jedi.functional.Functor;
import jedi.functional.Generator;

abstract class AbstractOption<T> implements Option<T> {

	@SuppressWarnings("unchecked")
	public Collection<T> asList() {
		return isEmpty() ? Collections.<T> emptyList() : Arrays.asList(get());
	}

	public T get() {
		throw new NoSuchElementException("None.get");
	}

	public T getOrElse(Generator<T> generator) {
		return isEmpty() ? generator.execute() : get();
	}

	public <R> Option<R> map(Functor<T, R> mappingFunction) {
		return isEmpty() ? Options.<R> None() : Options.Some(mappingFunction.execute(get()));
	}

	public void match(Command<T> someCommand, Command<None> noneCommand) {
		if (isEmpty()) {
			noneCommand.execute(None.NONE);
		} else {
			someCommand.execute(get());
		}
	}
}
