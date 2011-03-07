package jedi.functors;

import jedi.functional.Functor;
import jedi.functional.Functor2;

public class ComposeableFunctor2<T, U, R> implements Functor2<T, U, R> {
    private final Functor2<T, U, R> functor;

    public ComposeableFunctor2(final Functor2<T, U, R> functor) {
        this.functor = functor;
    }

    @Override
    public R execute(final T t, final U u) {
        return functor.execute(t, u);
    }

    /**
     * Function composition: this.andThen(g) applied to (t, u) == g(this.execute(t, u))
     */
    public <NEW_R> ComposeableFunctor2<T,U, NEW_R> andThen(final Functor<? super R, NEW_R> g) {
        return new ComposeableFunctor2<T, U, NEW_R>(new Functor2<T, U, NEW_R>() {
            @Override
            public NEW_R execute(T t, U u) {
                return g.execute(functor.execute(t, u));
            }
        });
    }
}