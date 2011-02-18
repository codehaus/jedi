package jedi.annotation.writer.factory;

import static jedi.functional.FunctionalPrimitives.head;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import jedi.annotation.processor.Environment;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.AnnotateableComparator;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.factorytype.FactoryType;
import jedi.annotation.writer.method.FactoryMethodWriter;
import jedi.functional.Comparables;

public class FactoryWriter {
	private StringWriter stringWriter;
	private JavaWriter writer;
	private final Environment environment;
	private final Map<Class<?>, FactoryMethodWriter> factoryMethodWritersByAnnotationClass;

	public FactoryWriter(final Environment environment, final Map<Class<?>, FactoryMethodWriter> factoryMethodWritersByAnnotationClass) {
		this.environment = environment;
		this.factoryMethodWritersByAnnotationClass = factoryMethodWritersByAnnotationClass;
	}

	private void endFactory(Annotateable annotateable, FactoryType factoryType) throws IOException {
		writer.println("}");
		writer.close();

		final PrintWriter realWriter = environment.createSourceFile(factoryType.getQualifiedTypeName(annotateable));
		realWriter.print(stringWriter.getBuffer());
		realWriter.close();
	}

	private String getFactoryClassName(Annotateable annotateable, FactoryType factoryType) {
		return factoryType.getTypeDeclaration(annotateable);
	}

	private String getPackageName(Annotateable annotateable) {
		final String packageName = annotateable.getPackage();
		return packageName.startsWith("java.") ? ("sith" + packageName.substring(4)) : packageName;
	}

	private void initialiseWriters(FactoryType factoryType) {
		stringWriter = new StringWriter();
		writer = new JavaWriter(stringWriter);
		for (final FactoryMethodWriter factoryMethodWriter : factoryMethodWritersByAnnotationClass.values()) {
			factoryMethodWriter.initialise(writer, factoryType);
		}
	}

	private void startFactory(Annotateable annotateable, FactoryType factoryType) {
		String packageName = getPackageName(annotateable);
		if (packageName.length() > 0) {
			writer.println("package " + packageName + ";");
			writer.println();
		}
		writer.println("public " + getFactoryClassName(annotateable, factoryType) + " {");
		factoryType.writeClassHeader(writer, annotateable);
	}

	public void write(final List<Annotateable> methods, FactoryType factoryType) {
		initialiseWriters(factoryType);
		try {
			startFactory(head(methods), factoryType);
			writeMethods(methods);
			endFactory(head(methods), factoryType);
		} catch (final IOException e) {
			environment.printError(e.getMessage());
		} catch (final FactoryWriterException fwex) {
			fwex.write(environment);
		}
	}

	private void writeMethods(final List<Annotateable> methods) {
		for (final Annotateable method : sort(methods)) {
			factoryMethodWritersByAnnotationClass.get(method.getAnnotationClass()).execute(method);
		}
	}

	private List<Annotateable> sort(List<Annotateable> methods) {
		return Comparables.sort(methods, new AnnotateableComparator());
	}
}
