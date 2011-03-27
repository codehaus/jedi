package jedi.tuple;

public final class Tuples {

	private Tuples() {}

	public static <A,B> Tuple2<A,B> tuple2(A a, B b) { return pair(a,b); }
	public static <A,B> Tuple2<A,B> pair(A a, B b) { return new Tuple2<A,B>(a,b); }

	public static <A,B,C> Tuple3<A,B,C> tuple3(A a, B b, C c) { return triple(a,b,c); }
	public static <A,B,C> Tuple3<A,B,C> triple(A a, B b, C c) { return new Tuple3<A,B,C>(a,b,c); };

	public static <A,B,C,D> Tuple4<A,B,C,D> tuple4(A a, B b, C c, D d) { return quad(a,b,c,d); }
	public static <A,B,C,D> Tuple4<A,B,C,D> quad(A a, B b, C c, D d) { return new Tuple4<A,B,C,D>(a,b,c,d); };
}
