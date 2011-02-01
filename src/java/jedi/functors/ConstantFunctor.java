package jedi.functors;

import jedi.functional.Functor0;

/**
 * A {@link }Functor0} that returns a constant.
 */
public class ConstantFunctor<R> implements Functor0<R> {
    
    private final R constant;

    /**
     * A convenient factory to create a constant functor.
     * @param constant
     * @param <R>
     * @return a functor that will always return <code>constant</code>
     */
    public static <R> ConstantFunctor<R> constant(R constant) {
        return new ConstantFunctor(constant);
    }

    public ConstantFunctor(R constant) {
        this.constant = constant;
    }

    public R execute() {
        return constant;
    }
}
