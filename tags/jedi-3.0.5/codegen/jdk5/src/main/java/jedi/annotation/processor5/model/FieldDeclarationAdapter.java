package jedi.annotation.processor5.model;

import static jedi.functional.Coercions.list;

import java.util.Collection;

import jedi.annotation.processor.model.Attribute;

import com.sun.mirror.type.TypeMirror;

public class FieldDeclarationAdapter extends AbstractMemberDeclarationAdapter<com.sun.mirror.declaration.FieldDeclaration> {
	public FieldDeclarationAdapter(com.sun.mirror.declaration.FieldDeclaration declaration) {
		super(declaration);
	}

	@Override
	protected TypeMirror getType() {
		return declaration.getType();
	}

	public Collection<Attribute> getParameters() {
		return list();
	}

	public String renderGenericTypeParameters() {
		return "";
	}
}
