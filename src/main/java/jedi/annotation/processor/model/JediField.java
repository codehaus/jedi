package jedi.annotation.processor.model;

import static jedi.functional.Coercions.list;

import java.util.List;

import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.method.FactoryMethodWriter;

public class JediField extends AbstractAnnotateable {
	public JediField(MemberDeclaration declaration, FactoryMethodWriter writer, String name) {
		super(declaration, writer, name);
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
