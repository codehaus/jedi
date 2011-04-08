package jedi.annotation.writer.factorytype;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.AttributeNameFunctor;
import jedi.annotation.writer.JavaWriter;
import jedi.assertion.Assert;

public class StaticFactoryType extends ConcreteFactoryType {
	private static final String DELEGATE_NAME = "$DELEGATE";

	@Override
	public String getSimpleTypeName(Annotateable annotateable) {
		return annotateable.getSimpleNameOfDeclaringType() + "StaticClosureFactory";
	}

	@Override
	public void writeMethodModifiers(JavaWriter writer) {
		writer.print("public static");
	}

	@Override
	public void writeMethodBody(ClosureFragmentWriter fragmentWriter, JavaWriter javaWriter) {
		javaWriter.openBlock();
		javaWriter.print("return " + DELEGATE_NAME + "." + fragmentWriter.getFactoryMethodName() + "(");
		fragmentWriter.writeFactoryMethodActualParameters(new AttributeNameFunctor());
		javaWriter.println(");");
		javaWriter.closeBlock();
	}

	@Override
	public void writeClassHeader(JavaWriter writer, Annotateable annotateable) {
		String interfaceFactoryName = new InterfaceFactoryType().getSimpleTypeName(annotateable);
		writer.println("private static " + interfaceFactoryName + " " + DELEGATE_NAME + " = new "
				+ new InstanceFactoryType().getSimpleTypeName(annotateable) + "();");
		writer.println();
		writer.print("public static void setDelegate(" + interfaceFactoryName + " newDelegate)").openBlock();
		writer.println(Assert.class.getName() + ".assertNotNull(newDelegate, \"newDelegate must not be null\");");
		writer.println(DELEGATE_NAME + " = newDelegate;");
		writer.closeBlock();
		writer.println();
		writer.print("public static void useDefaultDelegate()").openBlock();
		writer.println("setDelegate(new " + new InstanceFactoryType().getSimpleTypeName(annotateable) + "());");
		writer.closeBlock();
	}
}
