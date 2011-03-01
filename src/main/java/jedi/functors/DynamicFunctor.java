package jedi.functors;

import jedi.JediException;
import jedi.functional.Functor;
import jedi.functional.Functor2;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;


public class DynamicFunctor<T, R> implements Functor2<T, List<?>, R> {

    private Method method;

    public DynamicFunctor(Class<T> clazz, String methodName) {
        findMethod(clazz, methodName);
    }

    private void findMethod(Class<T> clazz, String methodName) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(methodName)) {
                method = m;
            }
        }
        if (method == null) throw new IllegalArgumentException("Method not found");
    }

    public R execute(T t) {
        return execute(t, Collections.<Object>emptyList());
    }

    @Override
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