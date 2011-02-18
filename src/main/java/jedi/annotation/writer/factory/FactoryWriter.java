package jedi.annotation.writer.factory;

import static jedi.functional.FunctionalPrimitives.head;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

import jedi.annotation.processor.Environment;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.AnnotateableComparator;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.factorytype.FactoryType;
import jedi.annotation.writer.method.FactoryMethodWriter;
import jedi.functional.Comparables;

public class FactoryWriter {
	private final StringWriter stringWriter = new StringWriter();
	private final JavaWriter writer = new JavaWriter(stringWriter);
	private final Environment environment;
	private final FactoryType factoryType;

	public FactoryWriter(final Environment environment, final FactoryType type, final Collection<FactoryMethodWriter> factoryMethodWriters) {
		this.environment = environment;
		this.factoryType = type;
		initialiseWriters(factoryMethodWriters);
	}

	private void endFactory(Annotateable annotateable) throws IOException {
		writer.println("}");
		writer.close();

		final PrintWriter realWriter = environment.createSourceFile(factoryType.getQualifiedTypeName(annotateable));
		realWriter.print(stringWriter.getBuffer());
		realWriter.close();
	}

	private String getFactoryClassName(Annotateable annotateable) {
		return factoryType.getTypeDeclaration(annotateable);
	}

	private String getPackageName(Annotateable annotateable) {
		final String packageName = annotateable.getPackage();
		return packageName.startsWith("java.") ? ("sith" + packageName.substring(4)) : packageName;
	}

	private void initialiseWriters(final Collection<FactoryMethodWriter> factoryMethodWriters) {
		for (final FactoryMethodWriter factoryMethodWriter : factoryMethodWriters) {
			factoryMethodWriter.initialise(writer, factoryType);
		}
	}

	private void startFactory(Annotateable annotateable) {
		String packageName = getPackageName(annotateable);
		if (packageName.length() > 0) {
			writer.println("package " + packageName + ";");
			writer.println();
		}
		writer.println("public " + getFactoryClassName(annotateable) + " {");
		factoryType.writeClassHeader(writer, annotateable);
	}

	public void write(final List<Annotateable> methods) {
		try {
			startFactory(head(methods));
			writeMethods(methods);
			endFactory(head(methods));
		} catch (final IOException e) {
			environment.printError(e.getMessage());
		} catch (final FactoryWriterException fwex) {
			fwex.write(environment);
		}
	}

	private void writeMethods(final List<Annotateable> methods) {
		for (final Annotateable method : sort(methods)) {
			method.writeFactoryMethod();
		}
	}

	private List<Annotateable> sort(List<Annotateable> methods) {
		return Comparables.sort(methods, new AnnotateableComparator());
	}
}
