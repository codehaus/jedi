package jedi.annotation.processor6.model;

import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import jedi.functional.Functor;

public class BoxerFunctor implements Functor<TypeMirror, String> {
	private final Types types;

	public BoxerFunctor(Types types) {
		this.types = types;
	}

	@Override
	public String execute(TypeMirror value) {
		return value.getKind().isPrimitive() ? asBoxed(value) : value.toString();
	}

	private String asBoxed(TypeMirror value) {
		return types.boxedClass((PrimitiveType) value).asType().toString();
	}
}
