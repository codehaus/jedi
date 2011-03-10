package jedi.annotation.processor6.model;

import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.join;

import java.util.Collection;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;

import jedi.functional.Functor;

class TypeDeclarationRenderer {
	public static String render(Collection<TypeParameterElement> collection) {
		return collection.isEmpty() ? "" : "<" + join(collect(collection, createTypeParameterElementRenderer()), ", ") + ">";
	}

	private static Functor<TypeParameterElement, String> createTypeParameterElementRenderer() {
		return new Functor<TypeParameterElement, String>() {
			public String execute(TypeParameterElement value) {
				return value.getSimpleName() + renderBounds(value.getBounds());
			}
		};
	}

	private static String renderBounds(Collection<? extends TypeMirror> bounds) {
		return bounds.isEmpty() ? "" : " extends " + join(bounds, "&");
	}

	public static String renderWithoutBounds(TypeElement typeElement) {
		return typeElement.getQualifiedName() + renderWithoutBounds(typeElement.getTypeParameters());
	}

	public static String renderWithoutBounds(Collection<? extends TypeParameterElement> typeParameters) {
		return typeParameters.isEmpty() ? "" : "<" + join(collectNames(typeParameters), ", ") + ">";
	}

	private static List<String> collectNames(Collection<? extends TypeParameterElement> typeParameters) {
		return collect(typeParameters, new Functor<TypeParameterElement, String>() {
			public String execute(TypeParameterElement value) {
				return value.getSimpleName().toString();
			}
		});
	}
}

