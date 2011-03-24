package jedi.functors;

import jedi.functional.Functor;

/**
 * A {@link Functor} that works with a single type
 */
public interface SingleTypeFunctor<T> extends Functor<T, T> {
}
