package jedi.annotation.processor5.model;

import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.join;

import java.util.Collection;
import java.util.List;

import jedi.functional.Functor;

import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.ReferenceType;

class TypeDeclarationRenderer {
	public static String render(Collection<TypeParameterDeclaration> typeParameters) {
		return typeParameters.isEmpty() ? "" : "<" + join(collect(typeParameters, createTypeParameterDeclarationRenderer()), ", ") + ">";
	}

	private static Functor<TypeParameterDeclaration, String> createTypeParameterDeclarationRenderer() {
		return new Functor<TypeParameterDeclaration, String>() {
			public String execute(TypeParameterDeclaration value) {
				return value.getSimpleName() + renderBounds(value.getBounds());
			}
		};
	}

	private static String renderBounds(Collection<ReferenceType> bounds) {
		return bounds.isEmpty() ? "" : " extends " + join(bounds, "&");
	}

	public static String renderWithoutBounds(TypeDeclaration declaration) {
		return declaration.getQualifiedName() + renderWithoutBounds(declaration.getFormalTypeParameters());
	}

	public static String renderWithoutBounds(Collection<TypeParameterDeclaration> typeParameters) {
		return typeParameters.isEmpty() ? "" : "<" + join(collectNames(typeParameters), ", ") + ">";
	}

	private static List<String> collectNames(Collection<TypeParameterDeclaration> typeParameters) {
		return collect(typeParameters, new Functor<TypeParameterDeclaration, String>() {
			public String execute(TypeParameterDeclaration value) {
				return value.getSimpleName();
			}
		});
	}
}
