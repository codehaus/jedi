package jedi.annotation.processor6.model;

import static jedi.functional.Coercions.asSet;

import java.io.File;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import jedi.annotation.processor.model.MemberDeclaration;

public abstract class AbstractMemberDeclarationAdapter<T extends Element> implements MemberDeclaration {

	protected final T element;

	public AbstractMemberDeclarationAdapter(T element) {
		this.element = element;
	}

	protected Element getEnclosingElement(ElementKind... kind) {
		return getEnclosingElement(element, asSet(kind));
	}

	protected Element getEnclosingElement(Element ancestor, Set<ElementKind> kind) {
		return kind.contains(ancestor.getKind()) ? ancestor : getEnclosingElement(ancestor.getEnclosingElement(), kind);
	}

	@Override
	public String getPackage() {
		return ((PackageElement) getEnclosingElement(ElementKind.PACKAGE)).getQualifiedName().toString();
	}

	protected TypeElement getDeclaringType() {
		return (TypeElement) getEnclosingElement(ElementKind.CLASS, ElementKind.INTERFACE);
	}

	@Override
	public String getDeclaringTypeWithUnboundedGenerics() {
		return TypeDeclarationRenderer.renderWithoutBounds(getDeclaringType());
	}

	@Override
	public String getQualifiedNameOfDeclaringType() {
		return getDeclaringType().getQualifiedName().toString();
	}

	@Override
	public String getSimpleNameOfDeclaringType() {
		return getDeclaringType().getSimpleName().toString();
	}

	@Override
	public String getOriginalName() {
		return element.getSimpleName().toString();
	}

	@Override
	public String getSimpleName() {
		return element.getSimpleName().toString();
	}

	protected abstract TypeMirror getType();

	@Override
	public String getDeclaredType() {
		return getType().toString();
	}

	@Override
	public String getBoxedDeclaredType() {
		return getType().getKind().isPrimitive() ? new BoxerFunctor().execute(getType()) : getDeclaredType();
	}

	@Override
	public boolean isVoid() {
		return element.asType().getKind() == TypeKind.VOID;
	}

	@Override
	public boolean isBoolean() {
		return element.asType().getKind() == TypeKind.BOOLEAN;
	}

	@Override
	public File getFile() {
		return null;
	}

	@Override
	public int getLine() {
		return 0;
	}

	@Override
	public int getColumn() {
		return 0;
	}
}