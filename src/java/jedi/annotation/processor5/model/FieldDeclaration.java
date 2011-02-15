package jedi.annotation.processor5.model;

import com.sun.mirror.type.TypeMirror;

public class FieldDeclaration extends AbstractMemberDeclaration<com.sun.mirror.declaration.FieldDeclaration> {
	public FieldDeclaration(com.sun.mirror.declaration.FieldDeclaration declaration) {
		super(declaration);
	}

	@Override
	protected TypeMirror getType() {
		return declaration.getType();
	}

	public String getName(boolean simplified) {
		return declaration.getSimpleName();
	}
}
