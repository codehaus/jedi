package jedi.annotation.writer.factory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.factorytype.FactoryType;
import jedi.annotation.writer.method.FactoryMethodWriter;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

public class FactoryWriter {
    private final StringWriter stringWriter = new StringWriter();
    private final JavaWriter writer = new JavaWriter(stringWriter);
    private final AnnotationProcessorEnvironment environment;
    private final TypeDeclaration typeDeclaration;
    private final FactoryType factoryType;

    public FactoryWriter(final AnnotationProcessorEnvironment environment, final TypeDeclaration typeDeclaration, final FactoryType type, final Map<AnnotationTypeDeclaration, FactoryMethodWriter> annotationTypeToFactoryMethodWriterMap) {
        this.environment = environment;
        this.typeDeclaration = typeDeclaration;
        this.factoryType = type;
        initialiseWriters(factoryType, annotationTypeToFactoryMethodWriterMap);
    }

    private void endFactory() throws IOException {
        writer.println("}");
        writer.close();

        final PrintWriter realWriter = environment.getFiler().createSourceFile(factoryType.getQualifiedTypeName(typeDeclaration));
        realWriter.print(stringWriter.getBuffer());
        realWriter.close();
    }

    private String getClassName() {
        return factoryType.getTypeDeclaration(typeDeclaration);
    }

    private String getPackageName() {
        final String packageName = typeDeclaration.getPackage().getQualifiedName();
        return packageName.startsWith("java.") ? ("sith" + packageName.substring(4)) : packageName;
    }

    private void initialiseWriters(final FactoryType factoryType, final Map<AnnotationTypeDeclaration, FactoryMethodWriter> annotationTypeToFactoryMethodWriterMap) {
        for (final FactoryMethodWriter factoryMethodWriter : annotationTypeToFactoryMethodWriterMap.values()) {
            factoryMethodWriter.initialise(writer, factoryType);
        }
    }

    private void startFactory() {
        if (getPackageName().length() > 0) {
            writer.println("package " + getPackageName() + ";");
            writer.println();
        }
        writer.println("public " + getClassName() + " {");
        factoryType.writeClassHeader(writer, typeDeclaration);
    }

    public void write(final List<Annotateable> methods) {
        try {
            startFactory();
            writeMethods(methods);
            endFactory();
        } catch (final IOException e) {
            environment.getMessager().printError(e.getMessage());
        } catch (final FactoryWriterException fwex) {
            fwex.write(environment.getMessager());
        }
    }

    private void writeMethods(final List<Annotateable> methods) {
        for (final Annotateable method : methods) {
            method.writeFactoryMethod();
        }
    }
}
