package jedi.option;

import static java.util.Collections.singletonList;
import static jedi.assertion.Assert.assertNotNull;
import static jedi.option.Options.Some;

import java.util.List;

import jedi.functional.Command;
import jedi.functional.Filter;
import jedi.functional.Functor;
import jedi.functional.Generator;

/**
 * Some represents a value of type <code>T</code> that exists.
 */
public final class Some<T> implements Option<T> {

	private final T value;

	public Some(T value) {
		assertNotNull(value, "Some value cannot be null, use None instead");
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

	/**
	 * @return the value, guaranteed to not be null.
	 */
	public T get() {
		return value;
	}

	public void match(OptionMatcher<T> matcher) {
		assertNotNull(matcher, "OptionMatcher must not be null");
		matcher.caseSome(value);
	}

	public <R> Option<R> map(Functor<T, R> mappingFunction) {
		R result = mappingFunction.execute(get());
		assertNotNull(result, "The result of the supplied mapping function is null.");
		return Some(result);
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

	public Option<T> filter(Filter<T> f) {
		return f.execute(get()) ? this : Options.<T>None();
	}
}
