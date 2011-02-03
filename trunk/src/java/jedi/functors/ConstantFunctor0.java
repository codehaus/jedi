package jedi.functors;

import jedi.functional.Functor0;

/**
 * A {@link }Functor0} that returns a constant.
 */
public class ConstantFunctor0<R> implements Functor0<R> {
    
    private final R c;

    /**
     * A convenient factory to create a constant functor.
     * @param constant
     * @param <R>
     * @return a functor that will always return <code>constant</code>
     */
    public static <R> ConstantFunctor0<R> constant(R constant) {
        return new ConstantFunctor0(constant);
    }

    public ConstantFunctor0(R constant) {
        this.c = constant;
    }

    public R execute() {
        return c;
    }
}
