package jedi.annotation.writer.factorytype;

import java.io.PrintWriter;

import jedi.annotation.writer.JavaWriter;

import com.sun.mirror.declaration.TypeDeclaration;

public class InterfaceFactoryType extends FactoryType {
    @Override
    public String getSimpleTypeName(TypeDeclaration typeDeclaration) {
        return "I" + typeDeclaration.getSimpleName() + "ClosureFactory";
    }

    @Override
    public String getTypeDeclaration(TypeDeclaration typeDeclaration) {
        return "interface " + getSimpleTypeName(typeDeclaration);
    }
    
    @Override
    public void writeMethodBody(ClosureFragmentWriter writer, JavaWriter javaWriter) {
        javaWriter.println(";");
    }
    
    @Override
    public void writeMethodModifiers(PrintWriter writer) {}
}
