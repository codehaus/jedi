package jedi.either;

import jedi.functional.Command;
import jedi.functional.Functor;

import java.util.Collections;
import java.util.List;

/**
 * Right typically represents a success value of Either - but not necessarily.
 */
public final class Right<A, B> extends Either<A, B> {

    public static final long serialVersionUID = 1L;

	private final B b;

	public Right(B b) {
		this.b = b;
	}

	@Override
	public boolean isRight() {
		return true;
	}

	@Override
	public <X> X fold(Functor<? super A, X> fa, Functor<? super B, X> fb) {
		return fb.execute(b);
	}

	@Override
	public void execute(Command<A> ca, Command<B> cb) {
		cb.execute(b);
	}

	@Override
	public Either<B, A> swap() {
		return new Left<B, A>(b);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}
		Right<?, ?> other = (Right<?, ?>) obj;
		return b.equals(other.b);
	}

	@Override
	public int hashCode() {
		return b.hashCode();
	}

	@Override
	public String toString() {
		return "Right:" + b;
	}

	@Override
	public List<A> asList() {
		return Collections.<A>emptyList();
	}

	@Override
	public <X> Either<X, B> map(Functor<? super A, X> f) {
		return new Right<X, B>(b);
	}

	@Override
	public <X> Either<X, B> flatMap(Functor<? super A, Either<X, B>> f) {
		return new Right<X, B>(b);
	}
}
