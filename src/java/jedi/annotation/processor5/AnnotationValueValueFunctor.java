package jedi.annotation.processor5;

import jedi.functional.Functor;

import com.sun.mirror.declaration.AnnotationValue;

public class AnnotationValueValueFunctor<T> implements Functor<AnnotationValue, T> {
	@SuppressWarnings("unchecked")
	public T execute(AnnotationValue value) {
		return (T) value.getValue();
	}
}
