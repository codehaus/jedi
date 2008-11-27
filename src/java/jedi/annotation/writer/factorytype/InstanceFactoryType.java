package jedi.annotation.writer.factorytype;

import java.io.PrintWriter;

import com.sun.mirror.declaration.TypeDeclaration;

public class InstanceFactoryType extends ConcreteFactoryType {
	@Override
	public String getTypeDeclaration(TypeDeclaration typeDeclaration) {
		return super.getTypeDeclaration(typeDeclaration) + " implements " + new InterfaceFactoryType().getSimpleTypeName(typeDeclaration);
	}

	@Override
	public String getSimpleTypeName(TypeDeclaration typeDeclaration) {
		return typeDeclaration.getSimpleName() + "ClosureFactory";
	}

	@Override
	public void writeMethodModifiers(PrintWriter writer) {
		writer.print("public");
	}
}
