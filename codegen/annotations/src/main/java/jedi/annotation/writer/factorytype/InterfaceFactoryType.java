package jedi.annotation.writer.factorytype;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.writer.JavaWriter;

public class InterfaceFactoryType extends FactoryType {
	@Override
	public String getSimpleTypeName(Annotateable annotateable) {
		return "I" + annotateable.getSimpleNameOfDeclaringType() + "ClosureFactory";
	}

	@Override
	public String getTypeDeclaration(Annotateable annotateable) {
		return "interface " + getSimpleTypeName(annotateable);
	}

	@Override
	public void writeMethodBody(ClosureFragmentWriter writer, JavaWriter javaWriter) {
		javaWriter.println(";");
	}

	@Override
	public void writeMethodModifiers(JavaWriter writer) {
	}
}
