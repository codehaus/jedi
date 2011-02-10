package jedi.annotation.writer.factorytype;

import java.io.PrintWriter;

import jedi.annotation.jedi.Annotateable;

public class InstanceFactoryType extends ConcreteFactoryType {
	@Override
	public String getTypeDeclaration(Annotateable annotateable) {
		return super.getTypeDeclaration(annotateable) + " implements " + new InterfaceFactoryType().getSimpleTypeName(annotateable);
	}

	@Override
	public String getSimpleTypeName(Annotateable annotateable) {
		return annotateable.getSimpleNameOfDeclaringType() + "ClosureFactory";
	}

	@Override
	public void writeMethodModifiers(PrintWriter writer) {
		writer.print("public");
	}
}
