package jedi.annotation.writer.factorytype;

import java.io.PrintWriter;

import jedi.annotation.writer.JavaWriter;
import jedi.assertion.Assert;

import com.sun.mirror.declaration.TypeDeclaration;


public class StaticFactoryType extends ConcreteFactoryType {
    private static final String DELEGATE_NAME = "$DELEGATE";
    
    @Override
    public String getSimpleTypeName(TypeDeclaration typeDeclaration) {
        return typeDeclaration.getSimpleName() + "StaticClosureFactory";
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
    public void writeClassHeader(PrintWriter writer, TypeDeclaration typeDeclaration) {
        String interfaceFactoryName = new InterfaceFactoryType().getSimpleTypeName(typeDeclaration);
        writer.println("\tprivate static " + interfaceFactoryName + " " + DELEGATE_NAME + " = new " + new InstanceFactoryType().getSimpleTypeName(typeDeclaration) + "();");
        writer.println();
        writer.println("\tpublic static void setDelegate(" + interfaceFactoryName + " newDelegate) {");
        writer.println("\t\t" + Assert.class.getName() + ".assertNotNull(newDelegate, \"newDelegate must not be null\");");
        writer.println("\t\t" + DELEGATE_NAME + " = newDelegate;");
        writer.println("\t}");
        writer.println();
        writer.println("\tpublic static void useDefaultDelegate() {");
        writer.println("\t\tsetDelegate(new " + new InstanceFactoryType().getSimpleTypeName(typeDeclaration) + "());");
        writer.println("\t}");
    }
}
