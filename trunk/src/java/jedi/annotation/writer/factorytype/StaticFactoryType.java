package jedi.annotation.writer.factorytype;

import java.io.PrintWriter;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.writer.JavaWriter;
import jedi.assertion.Assert;

public class StaticFactoryType extends ConcreteFactoryType {
	private static final String DELEGATE_NAME = "$DELEGATE";

	@Override
	public String getSimpleTypeName(Annotateable annotateable) {
		return annotateable.getSimpleNameOfDeclaringType() + "StaticClosureFactory";
	}

	@Override
	public void writeMethodModifiers(PrintWriter writer) {
		writer.print("public static");
	}

	@Override
	public void writeMethodBody(ClosureFragmentWriter fragmentWriter, JavaWriter javaWriter) {
		javaWriter.println(" {");
		javaWriter.print("\t\treturn " + DELEGATE_NAME + "." + fragmentWriter.getFactoryMethodName() + "(");
		fragmentWriter.writeFactoryMethodActualParameters();
		javaWriter.println(");");
		javaWriter.println("\t}");
	}

	@Override
	public void writeClassHeader(PrintWriter writer, Annotateable annotateable) {
		String interfaceFactoryName = new InterfaceFactoryType().getSimpleTypeName(annotateable);
		writer.println("\tprivate static " + interfaceFactoryName + " " + DELEGATE_NAME + " = new "
				+ new InstanceFactoryType().getSimpleTypeName(annotateable) + "();");
		writer.println();
		writer.println("\tpublic static void setDelegate(" + interfaceFactoryName + " newDelegate) {");
		writer.println("\t\t" + Assert.class.getName() + ".assertNotNull(newDelegate, \"newDelegate must not be null\");");
		writer.println("\t\t" + DELEGATE_NAME + " = newDelegate;");
		writer.println("\t}");
		writer.println();
		writer.println("\tpublic static void useDefaultDelegate() {");
		writer.println("\t\tsetDelegate(new " + new InstanceFactoryType().getSimpleTypeName(annotateable) + "());");
		writer.println("\t}");
	}
}
