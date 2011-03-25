package jedi.annotation.processor.model;

import static jedi.functional.Coercions.list;

import java.util.List;

import jedi.annotation.writer.JavaWriter;

public class JediField extends AbstractAnnotateable {
	public JediField(MemberDeclaration declaration, Class<?> annotationClass, String name) {
		super(declaration, annotationClass, name);
	}

	public List<Attribute> getCutParameters() {
		return list();
	}

	public String getName(boolean simplified) {
		return name;
	}

	public List<Attribute> getUncutParameters() {
		return list();
	}

	public void writeGenericTypeParameters(JavaWriter writer) {
	}

	public void writeInvocation(JavaWriter writer, String receiverName) {
		writer.print(receiverName + "." + getOriginalName());
	}
}
