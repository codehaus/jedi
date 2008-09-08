package jedi.annotation.writer.factorytype;

import java.io.PrintWriter;

import jedi.annotation.writer.JavaWriter;

import com.sun.mirror.declaration.TypeDeclaration;

public abstract class FactoryType {
    private String getPackage(final TypeDeclaration typeDeclaration) {
        return typeDeclaration.getPackage().getQualifiedName();
    }

    public String getQualifiedTypeName(final TypeDeclaration typeDeclaration) {
        String packageName = getPackage(typeDeclaration);
        if (packageName.startsWith("java.")) {
            packageName = "sith" + packageName.substring(4);
        }
        return packageName + "." + getSimpleTypeName(typeDeclaration);
    }

    public abstract String getSimpleTypeName(TypeDeclaration typeDeclaration);

    public abstract String getTypeDeclaration(TypeDeclaration typeDeclaration);

    public void writeClassHeader(final PrintWriter writer, final TypeDeclaration typeDeclaration) {
    }

    public abstract void writeMethodBody(ClosureFragmentWriter writer, JavaWriter javaWriter);

    public abstract void writeMethodModifiers(PrintWriter writer);
}
