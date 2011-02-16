package jedi.annotation.processor5.model;

import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.collect;

import java.util.Collection;

import jedi.annotation.processor.model.Attribute;
import jedi.functional.Functor;

import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.TypeMirror;

public class MethodDeclarationAdapter extends AbstractMemberDeclarationAdapter<com.sun.mirror.declaration.MethodDeclaration> {
	private final String name;

	public MethodDeclarationAdapter(com.sun.mirror.declaration.MethodDeclaration declaration) {
		this(declaration, null);
	}

	public MethodDeclarationAdapter(com.sun.mirror.declaration.MethodDeclaration declaration, String name) {
		super(declaration);
		this.name = (name == null || name.length() == 0 ? declaration.getSimpleName() : name);
	}

	@Override
	protected TypeMirror getType() {
		return declaration.getReturnType();
	}

	@Override
	public Collection<Attribute> getParameters() {
		return collect(declaration.getParameters(), new Functor<ParameterDeclaration, Attribute>() {
			public Attribute execute(ParameterDeclaration value) {
				return new Attribute(value.getType().toString(), new BoxerFunctor().execute(value.getType()), declaration.getSimpleName());
			}
		});
	}

	@SuppressWarnings("unchecked")
	private Collection<TypeParameterDeclaration> getGenericTypeParameters() {
		return append(declaration.getFormalTypeParameters(), declaration.getDeclaringType().getFormalTypeParameters());
	}

	@Override
	public String renderGenericTypeParameters() {
		return TypeDeclarationRenderer.render(getGenericTypeParameters());
	}
}
