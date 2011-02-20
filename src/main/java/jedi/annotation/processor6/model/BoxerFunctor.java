package jedi.annotation.processor6.model;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import jedi.functional.Functor;

public class BoxerFunctor implements Functor<TypeMirror, String> {
	@Override
	public String execute(TypeMirror value) {
		return value.getKind().isPrimitive() ? asBoxed(value.getKind()) : value.toString();
	}

	private String asBoxed(TypeKind kind) {
		switch (kind) {
		case BOOLEAN : return Boolean.class.getSimpleName();
		case BYTE : return Byte.class.getSimpleName();
		case CHAR : return Character.class.getSimpleName();
		case DOUBLE : return Double.class.getSimpleName();
		case FLOAT : return Float.class.getSimpleName();
		case INT : return Integer.class.getSimpleName();
		case LONG : return Long.class.getSimpleName();
		case SHORT : return Short.class.getSimpleName();
		default: throw new IllegalArgumentException("Unexpected PrimitiveType: " + kind.toString());
		}
	}
}
