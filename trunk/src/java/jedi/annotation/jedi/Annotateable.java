package jedi.annotation.jedi;

import java.util.List;

import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.writer.JavaWriter;

import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.util.SourcePosition;

public interface Annotateable {
	String getPackage();
	String getDeclaringTypeWithGenericsButWithoutBounds();
	String getQualifiedNameOfDeclaringType();
	String getSimpleNameOfDeclaringType();

	void writeFactoryMethod();

	SourcePosition getPosition();

	TypeMirror getType();

	List<Attribute> getUncutParameters();

	List<Attribute> getCutParameters();

	boolean isVoid();

	boolean isBoolean();

	void writeInvocation(JavaWriter printWriter, String receiverName);

	String getName(boolean simplified);

	public void writeGenericTypeParameters(JavaWriter writer);
}