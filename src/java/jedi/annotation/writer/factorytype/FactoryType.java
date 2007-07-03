package jedi.annotation.writer.factorytype;

import java.io.PrintWriter;

import jedi.annotation.writer.JavaWriter;

import com.sun.mirror.declaration.TypeDeclaration;

public abstract class FactoryType {
    public String getQualifiedTypeName(TypeDeclaration typeDeclaration) {
        return getPackage(typeDeclaration) + "." + getSimpleTypeName(typeDeclaration);
    }

    private String getPackage(TypeDeclaration typeDeclaration) {
        return typeDeclaration.getPackage().getQualifiedName();
    }
    
    public void writeClassHeader(PrintWriter writer, TypeDeclaration typeDeclaration) {}

    public abstract String getSimpleTypeName(TypeDeclaration typeDeclaration);
    public abstract String getTypeDeclaration(TypeDeclaration typeDeclaration);
    public abstract void writeMethodBody(ClosureFragmentWriter writer, JavaWriter javaWriter);
    public abstract void writeMethodModifiers(PrintWriter writer);
}
