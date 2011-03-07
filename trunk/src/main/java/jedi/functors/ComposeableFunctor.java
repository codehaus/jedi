package jedi.functors;

import jedi.functional.Functor;
import jedi.functional.Functor0;
import jedi.functional.Functor2;

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

    private final Functor<T, R> functor;

    public ComposeableFunctor(final Functor<T, R> functor) {
        this.functor = functor;
    }

    @Override
    public R execute(final T value) {
        return functor.execute(value);
    }

    /**
     * Function composition: this.andThen(g) applied to x == g(this.execute(value))
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

    public ComposeableFunctor0<R> o(Functor0<? extends T> g) {
        return composeable(g).andThen(functor);
    }

    public static <T, R> ComposeableFunctor<T, R> composeable(Functor<T, R> functor) {
        return new ComposeableFunctor<T, R>(functor);
    }

    public static <T, R> ComposeableFunctor<T, R> c(Functor<T, R> functor) {
        return composeable(functor);
    }

    public static <R> ComposeableFunctor0<R> composeable(Functor0<R> functor) {
        return new ComposeableFunctor0<R>(functor);
    }

    public static <R> ComposeableFunctor0<R> c(Functor0<R> functor) {
        return composeable(functor);
    }

    public static <T, U, R> ComposeableFunctor2<T, U, R> composeable(Functor2<T, U, R> functor) {
        return new ComposeableFunctor2<T, U, R>(functor);
    }

    public static <T, U, R> ComposeableFunctor2<T, U, R> c(Functor2<T, U, R> functor) {
        return composeable(functor);
    }
}