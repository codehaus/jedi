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
 * Projects an <code>Either</code> into a <code>Right</code>.
 * 
 */
public final class RightProjection<A, B> implements Iterable<B> {

	private final Either<A, B> either;

	public RightProjection(Either<A, B> either) {
		this.either = either;
	}

	/**
	 * Returns the value from this <code>Right</code> or throws
	 * <code>NoSuchElementException</code> if this is a <code>Left</code>.
	 */
	public B get() {
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
	 * Returns the value from this <code>Right</code> or throws
	 * <code>NoSuchElementException</code> if this is a <code>Left</code>.
	 */
	private A getLeft() {
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

	/**
     * @deprecated please use {@link #forEach(jedi.functional.Command)}
     * @see #forEach(jedi.functional.Command)
	 */
	public void foreach(final Command<B> c) {
		forEach(c);
	}
	
	/**
	 * Executes the given side-effect if this is a <code>Right</code>.
	 * 
	 * @param c
	 *            The side-effect to execute.
	 */
	public void forEach(final Command<B> c) {
		either.execute(new NullCommand<A>(), new Command<B>() {
			public void execute(B value) {
				c.execute(value);
			}
		});
	}

	/**
	 * Returns the value from this <code>Right</code> or the given argument if
	 * this is a <code>Left</code>.
	 */
	public <T extends B> B getOrElse(T or) {
		return either.isRight() ? get() : or;
	}

	/**
     * @deprecated please use {@link #forAll(Functor)}
     * @see #forAll(Functor)
	 */
	public Boolean forall(Functor<? super B, Boolean> f) {
		return forAll(f);
	}
	
	/**
	 * Returns <code>true</code> if <code>Left</code> or returns the result of
	 * the application of the given function to the <code>Right</code> value.
	 */
	public Boolean forAll(Functor<? super B, Boolean> f) {
		return either.isRight() ? f.execute(get()) : true;
	}

	/**
	 * Returns <code>false</code> if <code>Left</code> or returns the result of
	 * the application of the given function to the <code>Right</code> value.
	 */
	public Boolean exists(Functor<B, Boolean> f) {
		return either.isRight() ? f.execute(get()) : false;
	}

	/**
	 * Binds the given function across <code>Right</code>.
	 * 
	 * @param f The function to bind across <code>Right</code>.
	 */
	public <X> Either<A, X> flatMap(Functor<? super B, Either<A, X>> f) {
		return either.isRight() ? f.execute(get()) : new Left<A, X>(getLeft());
	}

	/**
	 * Maps the function argument through <code>Right</code>.
	 */
	public <X> Either<A, X> map(Functor<? super B, X> f) {
		return either.isRight() ? new Right<A, X>(f.execute(get())) : new Left<A, X>(getLeft());
	}

	/**
	 * Returns <code>None</code> if this is a <code>Left</code> or if the given
	 * predicate <code>p</code> does not hold for the right value, otherwise,
	 * returns a <code>Right</code>.
	 */
	public Option<? extends Either<A, B>> filter(Functor<B, Boolean> p) {
		if (either.isRight() && p.execute(get())) {
			return Options.some(new Right<A, B>(get()));
		} else {
			return Options.none();
		}
	}

	/**
	 * Returns a <code>Some</code> containing the <code>Right</code> value if it
	 * exists or a <code>None</code> if this is a <code>Left</code>.
	 */
	public Option<B> toOption() {
		if (either.isRight()) {
			return Options.some(get());
		} else {
			return Options.none();
		}
	}

	@SuppressWarnings("unchecked")
	public Iterator<B> iterator() {
		if (either.isRight()) {
			return Coercions.list(get()).iterator();
		} else {
			return new EmptyIterator<B>();
		}
	}

}
