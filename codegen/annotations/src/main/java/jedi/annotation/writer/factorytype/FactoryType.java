package jedi.annotation.writer.factorytype;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.writer.JavaWriter;

public abstract class FactoryType {
	public String getQualifiedTypeName(final Annotateable annotateable) {
		String packageName = annotateable.getPackage();
		if (packageName.startsWith("java.")) {
			packageName = "sith" + packageName.substring(4);
		}
        final String packageNameWithDot = packageName.length() > 0 ? packageName + "." : "";
        return packageNameWithDot + getSimpleTypeName(annotateable);
	}

	public abstract String getSimpleTypeName(Annotateable annotateable);

	public abstract String getTypeDeclaration(Annotateable annotateable);

	public void writeClassHeader(final JavaWriter writer, final Annotateable annotateable) {
	}

	public abstract void writeMethodBody(ClosureFragmentWriter writer, JavaWriter javaWriter);

	public abstract void writeMethodModifiers(JavaWriter writer);
}
