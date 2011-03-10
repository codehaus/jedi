package jedi.annotation.processor.model;

import static jedi.functional.Coercions.list;

import java.util.List;

import jedi.annotation.writer.JavaWriter;

public class JediField extends AbstractAnnotateable {
	public JediField(MemberDeclaration declaration, Class<?> annotationClass, String name) {
		super(declaration, annotationClass, name);
	}

	@Override
	public List<Attribute> getCutParameters() {
		return list();
	}

	@Override
	public String getName(boolean simplified) {
		return name;
	}

	@Override
	public List<Attribute> getUncutParameters() {
		return list();
	}

	@Override
	public void writeGenericTypeParameters(JavaWriter writer) {
	}

	@Override
	public void writeInvocation(JavaWriter writer, String receiverName) {
		writer.print(receiverName + "." + getOriginalName());
	}
}
