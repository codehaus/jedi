package jedi.functors;

import jedi.functional.Functor;

import static jedi.functors.ComposeableFunctor.composeable;

/**
 * A Functor that returns its argument.
 */
public class IdentityFunctor<T> implements Functor<T,T> {

    public static <T> ComposeableFunctor<T, T> identity() {
        return composeable(new IdentityFunctor<T>());
    }

    /**
     * @param value
     * @return value
     */
    public T execute(T value) {
        return value;
    }
}
