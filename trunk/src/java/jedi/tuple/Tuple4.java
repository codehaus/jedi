package jedi.tuple;

import static jedi.option.Options.option;

public class Tuple4<A,B,C,D> {

	private final A a;
	private final B b;
	private final C c;
	private final D d;

	public Tuple4(A a, B b, C c, D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
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

	public D d() {
		return d;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tuple3)) {
			return false;
		}
		Tuple4 other = (Tuple4) obj;
		return option(a).equals(option(other.a)) && option(b).equals(option(other.b)) && option(c).equals(option(other.c)) && option(d).equals(option(other.d));
	}

	@Override
	public int hashCode() {
		return option(a).hashCode() ^ option(b).hashCode() ^ option(c).hashCode() ^ option(d).hashCode();
	}
}
