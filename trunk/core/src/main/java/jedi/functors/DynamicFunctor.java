package jedi.functors;

import jedi.JediException;
import jedi.functional.Functor;
import jedi.functional.Functor2;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * This Functor reflectively executes a method on a type with zero or more arguments.
 * Examples
 * <br/>DynamicFunctor&lt;String, Integer&gt; stringLength = new DynamicFunctor(String.class, "length");
 * <br/>stringLength.execute("hello") returns 5
 * <br/>
 * <br/>DynamicFunctor&lt;String, Character&gt; stringCharAt = new DynamicFunctor(String.class, "charAt", Integer.TYPE);
 * <br/>stringCharAt.execute("hello", list(3)) returns 'l'
 */
public class DynamicFunctor<T, R> implements Functor2<T, List<?>, R>, Functor<T, R> {

    private final Method method;

    public DynamicFunctor(Class<T> clazz, String methodName, Class<?>... argTypes) {
        try {
            method = clazz.getMethod(methodName, argTypes);
            if (method == null) throw new IllegalArgumentException("Method not found");
        } catch (NoSuchMethodException e) {
            throw new JediException(e);
        }
    }

    public R execute(T t) {
        return execute(t, Collections.<Object>emptyList());
    }

    public R execute(T t, List<?> u) {
        try {
            if (method.getParameterTypes() == null || method.getParameterTypes().length == 0) {
                return (R) method.invoke(t);
            }
            return (R) method.invoke(t, u.toArray());
        } catch (Exception e) {
            throw new JediException(e);
        }
    }
}