package jedi.functors;

import jedi.functional.Functor;

/**
 * A functor that can be composed with other Functors.
 * For example, given:
 * <br/>Functor f that takes T and returns R
 * <br/>Functor g that takes R and returns NEW_R
 * <br/>new ComposeableFunctor(f).andThen(g).execute(x) == g(f(x))
 * <br/>or
 * <br/>composeable(f).andThen(g)
 * <br/>or
 * <br/>composeable(f).o(g)
 * <br/>or
 * <br/>c(f).o(g)
 * @param <T>
 * @param <R>
 */
public class ComposeableFunctor<T, R> implements Functor<T, R> {

    private Functor<T, R> functor;

    public ComposeableFunctor(Functor<T, R> functor) {
        this.functor = functor;
    }

    @Override
    public R execute(T value) {
        return functor.execute(value);
    }

    /**
     * Function composition: this.andThen(g) applied to x == g(this.execute(x))
     */
    public <NEW_R> ComposeableFunctor<T, NEW_R> andThen(final Functor<? super R, NEW_R> g) {
        return new ComposeableFunctor<T, NEW_R>(new Functor<T, NEW_R>() {
            @Override
            public NEW_R execute(T value) {
                return g.execute(functor.execute(value));
            }
        });
    }

    /**
     * Like the Haskell dot operator, this.o(g).execute(x) = this.execute(g.execute(x))
     */
    public <GT> ComposeableFunctor<GT, R> o(Functor<GT, ? extends T> g) {
        return composeable(g).andThen(functor);
    }

    public static <T, R> ComposeableFunctor<T, R> composeable(Functor<T, R> functor) {
        return new ComposeableFunctor<T, R>(functor);
    }

    public static <T, R> ComposeableFunctor<T, R> c(Functor<T, R> functor) {
        return composeable(functor);
    }
}