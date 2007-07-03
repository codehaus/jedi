package jedi.functors;

import jedi.functional.Functor2;

public class MinFunctor<T extends Comparable<? super T>> implements Functor2<T, T, T> {
    public T execute(T t, T u) {
        return t.compareTo(u) < 0 ? t : u;
    }
}
