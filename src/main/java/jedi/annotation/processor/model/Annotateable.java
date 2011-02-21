package jedi.annotation.processor.model;

import java.util.List;

import jedi.annotation.processor.Environment;
import jedi.annotation.writer.JavaWriter;

public interface Annotateable {
	String getPackage();

	String getDeclaringTypeWithUnboundedGenerics();

	String getQualifiedNameOfDeclaringType();

	String getSimpleNameOfDeclaringType();

	String getName(boolean simplified);

	String getOriginalName();

	String getDeclaredType();

	String getBoxedDeclaredType();

	boolean isVoid();

	boolean isBoolean();

	List<Attribute> getUncutParameters();

	List<Attribute> getCutParameters();

	void writeInvocation(JavaWriter printWriter, String receiverName);

	void writeGenericTypeParameters(JavaWriter writer);

	void showProcessingError(Environment environment, String message);

	Class<?> getAnnotationClass();
}