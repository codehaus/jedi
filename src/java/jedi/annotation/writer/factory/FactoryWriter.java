package jedi.annotation.writer.factory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.factorytype.FactoryType;
import jedi.annotation.writer.method.FactoryMethodWriter;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

public class FactoryWriter {
    private StringWriter stringWriter = new StringWriter();
    private JavaWriter writer = new JavaWriter(stringWriter);
    private final AnnotationProcessorEnvironment environment;
    private final TypeDeclaration typeDeclaration;
    private final FactoryType factoryType;
    
    public FactoryWriter(AnnotationProcessorEnvironment environment, TypeDeclaration typeDeclaration, FactoryType type, Map<AnnotationTypeDeclaration, FactoryMethodWriter> annotationTypeToFactoryMethodWriterMap) {
        this.environment = environment;
        this.typeDeclaration = typeDeclaration;
        this.factoryType = type;
        initialiseWriters(factoryType, annotationTypeToFactoryMethodWriterMap);
    }

    public void write(List<JediMethod> methods) {
        try {
            startFactory();
            writeMethods(methods);
            endFactory();
        } catch (IOException e) {
            environment.getMessager().printError(e.getMessage());
        } catch (FactoryWriterException fwex) {
            fwex.write(environment.getMessager());
        }
    }
    
    private void writeMethods(List<JediMethod> methods) {
        for (JediMethod method : methods) {
            method.write();
        }
    }

    private void initialiseWriters(FactoryType factoryType, Map<AnnotationTypeDeclaration, FactoryMethodWriter> annotationTypeToFactoryMethodWriterMap) {
        for (FactoryMethodWriter factoryMethodWriter : annotationTypeToFactoryMethodWriterMap.values()) {
            factoryMethodWriter.initialise(writer, factoryType);
        }
    }

    private void startFactory() throws IOException {
        if (getPackageName().length() > 0) {
            writer.println("package " + getPackageName() + ";");
            writer.println();
        }
        writer.println("public " + getClassName() + " {");
        factoryType.writeClassHeader(writer, typeDeclaration);
    }

    private String getClassName() {
        return factoryType.getTypeDeclaration(typeDeclaration);
    }

    private String getPackageName() {
        return typeDeclaration.getPackage().getQualifiedName();
    }
    
    private void endFactory() throws IOException {
        writer.println("}");
        writer.close();
        
        PrintWriter realWriter = environment.getFiler().createSourceFile(factoryType.getQualifiedTypeName(typeDeclaration));
        realWriter.print(stringWriter.getBuffer());
        realWriter.close();
    }
}
