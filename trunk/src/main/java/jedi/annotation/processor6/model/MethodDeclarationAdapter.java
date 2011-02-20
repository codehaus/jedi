package jedi.annotation.processor6.model;

import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.collect;

import java.util.Collection;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import jedi.annotation.processor.model.Attribute;
import jedi.functional.Functor;

public class MethodDeclarationAdapter extends AbstractMemberDeclarationAdapter<ExecutableElement> {
	public MethodDeclarationAdapter(ExecutableElement element) {
		super(element);
	}

	@Override
	protected TypeMirror getType() {
		return element.getReturnType();
	}

	@Override
	public Collection<Attribute> getParameters() {
		return collect(element.getParameters(), new Functor<VariableElement, Attribute>() {
			@Override
			public Attribute execute(VariableElement value) {
				return new Attribute(value.asType().toString(), new BoxerFunctor().execute(value.asType()), element.getSimpleName().toString());
			}
		});
	}

	@SuppressWarnings("unchecked")
	private Collection<TypeParameterElement> getGenericTypeParameters() {
		return append(element.getTypeParameters(), getDeclaringType().getTypeParameters());
	}

	@Override
	public String renderGenericTypeParameters() {
		return TypeDeclarationRenderer.render(getGenericTypeParameters());
	}
}
