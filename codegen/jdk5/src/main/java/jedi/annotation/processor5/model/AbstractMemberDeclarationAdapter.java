package jedi.annotation.processor5.model;

import java.io.File;

import jedi.annotation.processor.model.MemberDeclaration;

import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.VoidType;
import com.sun.mirror.util.SimpleTypeVisitor;
import com.sun.mirror.util.SourcePosition;

abstract class AbstractMemberDeclarationAdapter<T extends com.sun.mirror.declaration.MemberDeclaration> implements MemberDeclaration {
	protected final T declaration;

	public AbstractMemberDeclarationAdapter(T declaration) {
		this.declaration = declaration;
	}

	@Override
	public int hashCode() {
		return declaration.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}

		AbstractMemberDeclarationAdapter<?> that = (AbstractMemberDeclarationAdapter<?>) obj;
		return declaration.equals(that.declaration);
	}

	protected abstract TypeMirror getType();

	@Override
	public String getSimpleName() {
		return declaration.getSimpleName();
	}

	private TypeDeclaration getDeclaringType() {
		return declaration.getDeclaringType();
	}

	@Override
	public String getPackage() {
		return getDeclaringType().getPackage().getQualifiedName();
	}

	@Override
	public String getDeclaringTypeWithUnboundedGenerics() {
		return TypeDeclarationRenderer.renderWithoutBounds(getDeclaringType());
	}

	@Override
	public String getQualifiedNameOfDeclaringType() {
		return getDeclaringType().getQualifiedName();
	}

	@Override
	public String getSimpleNameOfDeclaringType() {
		return getDeclaringType().getSimpleName();
	}

	@Override
	public String getOriginalName() {
		return declaration.getSimpleName();
	}

	@Override
	public String getDeclaredType() {
		return getType().toString();
	}

	@Override
	public String getBoxedDeclaredType() {
		return new BoxerFunctor().execute(getType());
	}

	public boolean isVoid() {
		final boolean[] result = { false };
		getType().accept(new SimpleTypeVisitor() {
			@Override
			public void visitVoidType(VoidType arg0) {
				result[0] = true;
			}
		});

		return result[0];
	}

	public boolean isBoolean() {
		final boolean[] result = { false };

		getType().accept(new SimpleTypeVisitor() {
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

	@Override
	public File getFile() {
		return getPosition().file();
	}

	@Override
	public int getLine() {
		return getPosition().line();
	}

	@Override
	public int getColumn() {
		return getPosition().column();
	}

	private SourcePosition getPosition() {
		return declaration.getPosition();
	}
}
