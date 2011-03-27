package jedi.tuple;

import static jedi.option.Options.option;
import jedi.option.Option;

public class Tuple3<A,B,C> {

	private final A a;
	private final B b;
	private final C c;

	public Tuple3(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	public C c() {
		return c;
	}

	/**
	 * @return a Tuple3 with a, b and c wrapped in an {@link Option}
	 */
	public Tuple3<Option<A>, Option<B>, Option<C>> withOptions() {
		return new Tuple3<Option<A>, Option<B>, Option<C>>(option(a), option(b), option(c));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tuple3)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		Tuple3 other = (Tuple3) obj;
		return option(a).equals(option(other.a)) && option(b).equals(option(other.b)) && option(c).equals(option(other.c));
	}

	@Override
	public int hashCode() {
		return option(a).hashCode() ^ option(b).hashCode() ^ option(c).hashCode();
	}
}
