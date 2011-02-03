package jedi.functors;

import jedi.functional.Functor2;

/**
 * A {@link }Functor2} that returns a constant regardless of its arguments.
 */
public class ConstantFunctor2<R> implements Functor2<Object, Object, R> {

    private final R r;

    public static <R> ConstantFunctor2<R> constant(R c) {
        return new ConstantFunctor2(c);
    }

    public ConstantFunctor2(R r) {
        this.r = r;
    }

    public R execute(Object a, Object b) {
        return r;
    }

}
