package jedi.annotation.jedi;

import jedi.annotation.processor.Environment;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor5.model.BoxerFunctor;
import jedi.annotation.writer.TypeDeclarationRenderer;
import jedi.annotation.writer.method.FactoryMethodWriter;

import com.sun.mirror.declaration.MemberDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.VoidType;
import com.sun.mirror.util.SimpleTypeVisitor;
import com.sun.mirror.util.SourcePosition;

abstract class AbstractAnnotateable<T extends MemberDeclaration> implements Annotateable {
	private final FactoryMethodWriter factoryMethodWriter;
	protected String name;
	protected final T declaration;

	public AbstractAnnotateable(T declaration, FactoryMethodWriter writer, String name) {
		this.declaration = declaration;
		factoryMethodWriter = writer;
		this.name = (name == null || name.length() == 0 ? declaration.getSimpleName() : name);
	}

	public void writeFactoryMethod() {
		factoryMethodWriter.execute(this);
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

		AbstractAnnotateable<?> that = (AbstractAnnotateable<?>) obj;
		return declaration.equals(that.declaration) && factoryMethodWriter.equals(that.factoryMethodWriter) && name.equals(that.name);
	}

	protected String getOriginalName() {
		return declaration.getSimpleName();
	}

	private SourcePosition getPosition() {
		return declaration.getPosition();
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
	public String getPackage() {
		return getDeclaringType().getPackage().getQualifiedName();
	}

	private TypeDeclaration getDeclaringType() {
		return declaration.getDeclaringType();
	}

	@Override
	public String getDeclaredType() {
		return getType().toString();
	}

	@Override
	public String getBoxedDeclaredType() {
		return new BoxerFunctor().execute(getType());
	}

	protected abstract TypeMirror getType();

	@Override
	public void showProcessingError(Environment environment, String message) {
		environment.printError(getPosition().file(), getPosition().line(), getPosition().column(), message);
	}
}