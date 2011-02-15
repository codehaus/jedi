package jedi.functional;

public interface Functor<T, R> {
	R execute(T value);
}
