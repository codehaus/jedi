package jedi.annotation.writer.factorytype;

import jedi.annotation.writer.JavaWriter;

import com.sun.mirror.declaration.TypeDeclaration;

public abstract class ConcreteFactoryType extends FactoryType {
    @Override
    public String getTypeDeclaration(TypeDeclaration typeDeclaration) {
        return "class " + getSimpleTypeName(typeDeclaration);
    }

    @Override
    public void writeMethodBody(ClosureFragmentWriter fragmentWriter, JavaWriter javaWriter) {
        javaWriter.println(" {");
        fragmentWriter.writeLocalClass("Closure");
        javaWriter.println("\t\treturn new Closure();");
        javaWriter.println("\t}");
    }
}
