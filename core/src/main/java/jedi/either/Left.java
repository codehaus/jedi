package jedi.either;

import jedi.functional.Command;
import jedi.functional.Functor;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Left typically represents a failure condition of Either - but not
 * necessarily.
 */
public final class Left<A, B> extends Either<A, B> {

    public static final long serialVersionUID = 1L;

	private final A a;

	public Left(A a) {
		this.a = a;
	}

	@Override
	public boolean isLeft() {
		return true;
	}

	@Override
	public <X> X fold(Functor<? super A, X> fa, Functor<? super B, X> fb) {
		return fa.execute(a);
	}

	@Override
	public void execute(Command<A> ca, Command<B> cb) {
		ca.execute(a);
	}

	@Override
	public Either<B, A> swap() {
		return new Right<B, A>(a);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}
		Left<?, ?> other = (Left<?, ?>) obj;
		return a.equals(other.a);
	}

	@Override
	public int hashCode() {
		return a.hashCode();
	}

	@Override
	public String toString() {
		return "Left:" + a;
	}

	@Override
	public List<A> asList() {
		return singletonList(a);
	}

	@Override
	public <X> Either<X, B> map(Functor<? super A, X> f) {
		return new Left<X, B>(f.execute(a));
	}

	@Override
	public <X> Either<X, B> flatMap(Functor<? super A, Either<X, B>> f) {
		return f.execute(a);
	}

}
