package jedi.functional;

public interface Functor2<T, U, R> {
	R execute(T t, U u);
}
