package jedi.annotation.writer.factorytype;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.writer.JavaWriter;

public abstract class ConcreteFactoryType extends FactoryType {
	@Override
	public String getTypeDeclaration(Annotateable annotateable) {
		return "class " + getSimpleTypeName(annotateable);
	}

	@Override
	public void writeMethodBody(ClosureFragmentWriter fragmentWriter, JavaWriter javaWriter) {
		javaWriter.println(" {");
		fragmentWriter.writeLocalClass("Closure");
		javaWriter.println("\t\treturn new Closure();");
		javaWriter.println("\t}");
	}
}
