package jedi.either;

import java.util.Iterator;
import java.util.NoSuchElementException;

import jedi.functional.Coercions;
import jedi.functional.Command;
import jedi.functional.EmptyIterator;
import jedi.functional.Functor;
import jedi.functional.NullCommand;
import jedi.option.Option;
import jedi.option.Options;

/**
 * Projects an <code>Either</code> into a <code>Left</code>.
 * 
 */
public final class LeftProjection<A, B> implements Iterable<A> {

	private final Either<A, B> either;

	public LeftProjection(Either<A, B> either) {
		this.either = either;
	}

	/**
	 * Returns the value from this <code>Left</code> or throws
	 * <code>NoSuchElementException</code> if this is a
	 * <code>Right</code>.
	 */
	public A get() {
		return either.fold(new Functor<A, A>() {
			public A execute(A value) {
				return value;
			}
		}, new Functor<B, A>() {
			public A execute(B value) {
				throw new NoSuchElementException();
			}
		});
	}

	private B getRight() {
		return either.fold(new Functor<A, B>() {
			public B execute(A value) {
				throw new NoSuchElementException();
			}
		}, new Functor<B, B>() {
			public B execute(B value) {
				return value;
			}
		});
	}

	/**
	 * Executes the given side-effect if this is a <code>Left</code>.
	 * 
	 * @param c
	 *            The side-effect to execute.
	 */
	public void foreach(final Command<A> c) {
		either.execute(new Command<A>() {
			public void execute(A value) {
				c.execute(value);
			}
		}, new NullCommand<B>());
	}

	/**
	 * Returns the value from this <code>Left</code> or the given argument if
	 * this is a <code>Right</code>.
	 */
	public <T extends A> A getOrElse(T or) {
		return either.isLeft() ? get() : or;
	}

	/**
	 * Returns <code>true</code> if <code>Right</code> or returns the result of
	 * the application of the given function to the <code>Left</code> value.
	 */
	public Boolean forall(Functor<A, Boolean> f) {
		return either.isLeft() ? f.execute(get()) : true;
	}

	/**
	 * Returns <code>false</code> if <code>Right</code> or returns the result of
	 * the application of the given function to the <code>Left</code> value.
	 */
	public Boolean exists(Functor<A, Boolean> f) {
		return either.isLeft() ? f.execute(get()) : false;
	}

	/**
	 * Binds the given function across <code>Left</code>.
	 * 
	 * @param The
	 *            function to bind across <code>Left</code>.
	 */
	public <X> Either<X, B> flatMap(Functor<A, Either<X, B>> f) {
		return either.isLeft() ? f.execute(get()) : new Right<X, B>(getRight());
	}

	/**
	 * Maps the function argument through <code>Left</code>.
	 */
	public <X> Either<X, B> map(Functor<A, X> f) {
		return either.isLeft() ? new Left<X, B>(f.execute(get())) : new Right<X, B>(getRight());
	}

	/**
	 * Returns <code>None</code> if this is a <code>Right</code> or if the given
	 * predicate <code>p</code> does not hold for the left value, otherwise,
	 * returns a <code>Left</code>.
	 */
	public Option<? extends Either<A, B>> filter(Functor<A, Boolean> p) {
		if (either.isLeft() && p.execute(get())) {
			return Options.some(new Left<A, B>(get()));
		} else {
			return Options.none();
		}
	}

	/**
	 * Returns a <code>Some</code> containing the <code>Left</code> value if it
	 * exists or a <code>None</code> if this is a <code>Right</code>.
	 */
	public Option<A> toOption() {
		if (either.isLeft()) {
			return Options.some(get());
		} else {
			return Options.none();
		}
	}

	@SuppressWarnings("unchecked")
	public Iterator<A> iterator() {
		if (either.isLeft()) {
			return Coercions.list(get()).iterator();
		} else {
			return new EmptyIterator<A>();
		}
	}

}
