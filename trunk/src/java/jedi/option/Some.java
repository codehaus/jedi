package jedi.option;

import static java.util.Collections.singletonList;
import static jedi.assertion.Assert.assertNotNull;
import static jedi.option.Options.some;

import java.util.Iterator;
import java.util.List;

import jedi.functional.Command;
import jedi.functional.Command0;
import jedi.functional.Filter;
import jedi.functional.Functor;
import jedi.functional.Functor0;

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

	public T getOrElse(Functor0<? extends T> generator) {
		return get();
	}

	public T getOrElse(T defaultValue) {
		return get();
	}

	/**
	 * Get the value this Some contains.
	 * 
	 * @return the value, guaranteed to not be null.
	 */
	public T get() {
		return value;
	}

	public void match(OptionMatcher<? super T> matcher) {
		assertNotNull(matcher, "OptionMatcher must not be null");
		matcher.caseSome(value);
	}

	public void match(Command<? super T> someCommand, Command0 noneCommand) {
		someCommand.execute(get());
	}

	public <R1, R2 extends R1> R1 match(Functor<? super T, R1> someFunctor, Functor0<R2> noneFunctor) {
		return someFunctor.execute(get());
	}

	public <R> Option<R> map(Functor<? super T, R> mappingFunction) {
		R result = mappingFunction.execute(get());
		return result == null ? Options.<R>none() : some(result);
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

	public void forEach(Command<? super T> command) {
		command.execute(get());
	}

	public Option<T> filter(Filter<? super T> f) {
		return f.execute(get()) ? this : Options.<T> none();
	}

	public Iterator<T> iterator() {
		return asList().iterator();
	}

	public T unsafeGet() {
		return value;
	}
}
