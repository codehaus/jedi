package jedi.option;

import static java.util.Collections.singletonList;
import static jedi.option.Options.Some;

import java.util.List;

import jedi.functional.Command;
import jedi.functional.Functor;
import jedi.functional.Generator;

public final class Some<T> implements Option<T> {

	private final T value;

	public Some(T value) {
		this.value = value;
	}

	public List<T> asList() {
		return singletonList(get());
	}

	public T getOrElse(Generator<T> generator) {
		return get();
	}
	
	public T getOrElse(final T defaultValue) {
		return get();
	}

	public T get() {
		return value;
	}

	public void match(OptionMatcher<T> matcher) {
		matcher.caseSome(value);
	}

	public <R> Option<R> map(Functor<T, R> mappingFunction) {
		return Some(mappingFunction.execute(get()));
	}

	public void match(Command<T> someCommand, Command<None<T>> noneCommand) {
		someCommand.execute(get());
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		return obj instanceof Some && get().equals(((Some) obj).get());
	}

	@Override
	public int hashCode() {
		return get().hashCode();
	}

	@Override
	public String toString() {
		return "Some: " + get();
	}

	public void forEach(Command<T> command) {
		command.execute(get());
	}
}
