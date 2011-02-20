package jedi.annotation.processor6.model;

import static jedi.functional.Coercions.set;

import java.util.Collection;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import jedi.annotation.processor.model.Attribute;

public class FieldDeclarationAdapter extends AbstractMemberDeclarationAdapter<VariableElement> {
	public FieldDeclarationAdapter(VariableElement element) {
		super(element);
	}

	@Override
	protected TypeMirror getType() {
		return element.asType();
	}

	@Override
	public Collection<Attribute> getParameters() {
		return set();
	}

	@Override
	public String renderGenericTypeParameters() {
		return "";
	}
}
