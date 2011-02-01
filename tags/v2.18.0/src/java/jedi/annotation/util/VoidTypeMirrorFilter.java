package jedi.annotation.util;

import jedi.functional.Filter;

import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.VoidType;
import com.sun.mirror.util.SimpleTypeVisitor;

public class VoidTypeMirrorFilter implements Filter<TypeMirror> {
	public Boolean execute(TypeMirror returnType) {
		final boolean[] result = { false };
		returnType.accept(new SimpleTypeVisitor() {
			@Override
			public void visitVoidType(VoidType arg0) {
				result[0] = true;
			}
		});

		return result[0];
	}
}
