package jedi.either;

import java.util.List;

import jedi.functional.Command;
import jedi.functional.Functor;

/**
 * The <code>Either</code> type represents a value of one of two possible types
 * (a disjoint union) and was Inspired by Scala's Either.
 */
public abstract class Either<A, B> {

	/**
	 * If the condition satisfies, return the given A in <code>Left</code>,
	 * otherwise, return the given B in <code>Right</code>.
	 */
	public static <A, B> Either<A, B> cond(boolean cond, A a, B b) {
		return cond ? new Left<A, B>(a) : new Right<A, B>(b);
	}

	public boolean isLeft() {
		return false;
	}

	public boolean isRight() {
		return false;
	}

	public LeftProjection<A, B> left() {
		return new LeftProjection<A, B>(this);
	}

	public RightProjection<A, B> right() {
		return new RightProjection<A, B>(this);
	}

	/**
	 * Deconstruction of the type.
	 * 
	 * @param fa
	 *            the functor to apply if this is a Left
	 * @param fb
	 *            the functor to apply if this is a Left
	 * @return the result of applying the appropriate functor.
	 */
	public abstract <X> X fold(Functor<A, X> fa, Functor<B, X> fb);

	public abstract void execute(Command<A> ca, Command<B> cb);

	public abstract Either<B, A> swap();

	/**
	 * @return Singleton list if Lift or empty list if Right.
	 */
	public abstract List<A> asList();

	/**
	 * Maps the function argument through <code>Left</code>.
	 */
	public abstract <X> Either<X, B> map(Functor<A, X> f);
}
