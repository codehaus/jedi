package jedi.annotation.jedi;

import java.util.List;

import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.processor.Environment;
import jedi.annotation.writer.JavaWriter;

public interface Annotateable {
	String getPackage();
	String getDeclaringTypeWithUnboundedGenerics();
	String getQualifiedNameOfDeclaringType();
	String getSimpleNameOfDeclaringType();

	String getName(boolean simplified);
	String getDeclaredType();
	String getBoxedDeclaredType();
	boolean isVoid();
	boolean isBoolean();

	List<Attribute> getUncutParameters();

	List<Attribute> getCutParameters();

	void writeFactoryMethod();
	void writeInvocation(JavaWriter printWriter, String receiverName);
	void writeGenericTypeParameters(JavaWriter writer);

	void showProcessingError(Environment environment, String message);
}