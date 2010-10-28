package jedi.tuple;

import static jedi.option.Options.option;

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tuple2)) {
			return false;
		}
		Tuple2 other = (Tuple2) obj;
		return option(a).equals(option(other.a)) && option(b).equals(option(other.b));
	}

	@Override
	public int hashCode() {
		return option(a).hashCode() ^ option(b).hashCode();
	}
}
