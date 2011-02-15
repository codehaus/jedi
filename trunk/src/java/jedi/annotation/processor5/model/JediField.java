package jedi.annotation.processor5.model;

import static jedi.functional.Coercions.list;

import java.util.List;

import jedi.annotation.processor.model.Attribute;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.method.FactoryMethodWriter;

public class JediField extends AbstractAnnotateable<FieldDeclaration> {
	public JediField(FieldDeclaration declaration, FactoryMethodWriter writer, String name) {
		super(declaration, writer, name);
	}

	@Override
	public List<Attribute> getCutParameters() {
		return list();
	}

	@Override
	public String getName(boolean simplified) {
		return declaration.getSimpleName();
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
