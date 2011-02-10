package jedi.annotation.writer.factorytype;

import java.io.PrintWriter;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.writer.JavaWriter;

public abstract class FactoryType {
	public String getQualifiedTypeName(final Annotateable annotateable) {
		String packageName = annotateable.getPackage();
		if (packageName.startsWith("java.")) {
			packageName = "sith" + packageName.substring(4);
		}
		return packageName + "." + getSimpleTypeName(annotateable);
	}

	public abstract String getSimpleTypeName(Annotateable annotateable);

	public abstract String getTypeDeclaration(Annotateable annotateable);

	public void writeClassHeader(final PrintWriter writer, final Annotateable annotateable) {
	}

	public abstract void writeMethodBody(ClosureFragmentWriter writer, JavaWriter javaWriter);

	public abstract void writeMethodModifiers(PrintWriter writer);
}
