package jedi.functors;

import jedi.functional.Functor;

/**
 * A {@link Functor} that returns a constant regardless of its argument.
 */
public class ConstantFunctor<R> implements Functor<Object, R> {

    private final R r;

    /**
     * A convenient factory to create a constant functor.
     * @param c
     * @return a functor that will always return <code>c</code>
     */
    public static <R> ConstantFunctor<R> constant(R c) {
        return new ConstantFunctor<R>(c);
    }

    public ConstantFunctor(R r) {
        this.r = r;
    }

    public R execute(Object value) {
        return r;
    }

}