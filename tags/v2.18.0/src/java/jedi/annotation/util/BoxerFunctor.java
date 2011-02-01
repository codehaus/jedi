package jedi.annotation.util;

import java.util.HashMap;
import java.util.Map;

import jedi.functional.Functor;

import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.util.SimpleTypeVisitor;

public class BoxerFunctor implements Functor<TypeMirror, String> {
	private static final Map<PrimitiveType.Kind, Class<?>> BOXER = new HashMap<PrimitiveType.Kind, Class<?>>();

	static {
		BOXER.put(PrimitiveType.Kind.BOOLEAN, Boolean.class);
		BOXER.put(PrimitiveType.Kind.BYTE, Byte.class);
		BOXER.put(PrimitiveType.Kind.CHAR, Character.class);
		BOXER.put(PrimitiveType.Kind.DOUBLE, Double.class);
		BOXER.put(PrimitiveType.Kind.FLOAT, Float.class);
		BOXER.put(PrimitiveType.Kind.INT, Integer.class);
		BOXER.put(PrimitiveType.Kind.LONG, Long.class);
		BOXER.put(PrimitiveType.Kind.SHORT, Short.class);
	}

	public String execute(TypeMirror type) {
		final String[] name = new String[1];

		type.accept(new SimpleTypeVisitor() {
			@Override
			public void visitPrimitiveType(PrimitiveType arg0) {
				name[0] = BOXER.get(arg0.getKind()).getName();
			}

			@Override
			public void visitTypeMirror(TypeMirror arg0) {
				name[0] = arg0.toString();
			}
		});

		return name[0];
	}
}
