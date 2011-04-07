package jedi.tuple;

import static jedi.option.Options.option;
import jedi.option.Option;

public class Tuple2<A,B> {
	private final A a;
	private final B b;

	public Tuple2(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	/**
	 * @return a Tuple2 with a and b wrapped in an {@link Option}
	 */
	public Tuple2<Option<A>, Option<B>> withOptions() {
		return new Tuple2<Option<A>, Option<B>>(option(a), option(b));
	}

	/**
	 * Swap a and b
	 */
	public Tuple2<B, A> swap() {
		return new Tuple2<B, A>(b, a);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tuple2)) {
			return false;
		}
		Tuple2<?, ?> other = (Tuple2<?, ?>) obj;
		return option(a).equals(option(other.a)) && option(b).equals(option(other.b));
	}

	@Override
	public int hashCode() {
		return option(a).hashCode() ^ option(b).hashCode();
	}

	@Override
	public String toString() {
		return "(" + a + "," + b + ")";
	}
}
