package jedi.functors;

import jedi.functional.Functor;

/**
 * A Functor that returns its argument.
 */
public class IdentityFunctor<T> implements Functor<T,T> {

    public static <T> IdentityFunctor<T> identity() {
        return new IdentityFunctor<T>();
    }

    /**
     * @param value
     * @return value
     */
    public T execute(T value) {
        return value;
    }
}
