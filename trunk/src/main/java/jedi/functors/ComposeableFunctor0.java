package jedi.functors;

import jedi.functional.Functor;
import jedi.functional.Functor0;

public class ComposeableFunctor0<R> implements Functor0<R> {

    private Functor0<R> functor;

    public ComposeableFunctor0(Functor0 functor) {
        this.functor = functor;
    }

    public <T, NEW_R> ComposeableFunctor0<NEW_R> andThen(final Functor<? super R, NEW_R> g) {
        return new ComposeableFunctor0<NEW_R>(new Functor0<NEW_R>() {
            @Override
            public NEW_R execute() {
                return g.execute(functor.execute());
            }
        });
    }

    @Override
    public R execute() {
        return functor.execute();
    }
}
