package jedi.annotation.util;

import jedi.functional.Filter;

import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.util.SimpleTypeVisitor;

public class BooleanTypeMirrorFilter implements Filter<TypeMirror> {
	public Boolean execute(TypeMirror typeMirror) {
		final boolean[] result = { false };

		typeMirror.accept(new SimpleTypeVisitor() {
			@Override
			public void visitPrimitiveType(PrimitiveType arg0) {
				result[0] = arg0.getKind().equals(PrimitiveType.Kind.BOOLEAN);
			}

			@Override
			public void visitClassType(ClassType arg0) {
				result[0] = arg0.getDeclaration().getQualifiedName().equals("java.lang.Boolean");
			}
		});

		return result[0];
	}
}
