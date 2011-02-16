package jedi.annotation.processor.model;

import jedi.annotation.processor.Environment;
import jedi.annotation.writer.method.FactoryMethodWriter;

abstract class AbstractAnnotateable implements Annotateable {
	private final FactoryMethodWriter factoryMethodWriter;
	protected String name;
	protected final MemberDeclaration declaration;

	public AbstractAnnotateable(MemberDeclaration declaration, FactoryMethodWriter writer, String name) {
		this.declaration = declaration;
		factoryMethodWriter = writer;
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

		AbstractAnnotateable that = (AbstractAnnotateable) obj;
		return declaration.equals(that.declaration) && factoryMethodWriter.equals(that.factoryMethodWriter) && name.equals(that.name);
	}

	protected String getOriginalName() {
		return declaration.getOriginalName();
	}

	public boolean isVoid() {
		return declaration.isVoid();
	}

	public boolean isBoolean() {
		return declaration.isBoolean();
	}

	public String getDeclaringTypeWithUnboundedGenerics() {
		return declaration.getDeclaringTypeWithUnboundedGenerics();
	}

	@Override
	public String getQualifiedNameOfDeclaringType() {
		return declaration.getQualifiedNameOfDeclaringType();
	}

	@Override
	public String getSimpleNameOfDeclaringType() {
		return declaration.getSimpleNameOfDeclaringType();
	}

	@Override
	public String getPackage() {
		return declaration.getPackage();
	}

	@Override
	public String getDeclaredType() {
		return declaration.getDeclaredType();
	}

	@Override
	public String getBoxedDeclaredType() {
		return declaration.getBoxedDeclaredType();
	}

	@Override
	public void showProcessingError(Environment environment, String message) {
		environment.printError(declaration.getFile(), declaration.getLine(), declaration.getColumn(), message);
	}

	public void writeFactoryMethod() {
		factoryMethodWriter.execute(this);
	}
}