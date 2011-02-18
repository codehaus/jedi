package jedi.annotation.processor5.model;

import static jedi.functional.Coercions.asSet;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.select;

import java.util.List;
import java.util.Set;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.JediMethod;
import jedi.filters.NotNullFilter;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.util.SourcePosition;

public class SithAnnotation extends AnnotationMirrorInterpreter {
	private final AnnotationTypeDeclaration typeDeclaration;
	private final AnnotationProcessorEnvironment environment;

	public SithAnnotation(AnnotationTypeDeclaration typeDeclaration, AnnotationMirror annotationMirror,
			AnnotationProcessorEnvironment environment) {
		super(annotationMirror);
		this.typeDeclaration = typeDeclaration;
		this.environment = environment;
	}

	public Set<Annotateable> getMethodDeclarations(final Class<?> annotationClass) {
		return asSet(collect(select(getRequiredMethods(), new NotNullFilter<MethodDeclaration>()),
				new Functor<MethodDeclaration, Annotateable>() {
			public Annotateable execute(MethodDeclaration value) {
				return new JediMethod(new jedi.annotation.processor5.model.MethodDeclarationAdapter(value), annotationClass);
			}
		}));
	}

	public SourcePosition getPosition() {
		return typeDeclaration.getPosition();
	}

	TypeDeclaration getType() {
		return ((DeclaredType) getValue("type")).getDeclaration();
	}

	@SuppressWarnings("unchecked")
	private List<MethodDeclaration> getRequiredMethods() {
		return collect((List<AnnotationValue>) getValue("methods"), new Functor<AnnotationValue, MethodDeclaration>() {
			public MethodDeclaration execute(AnnotationValue value) {
				return getMethodDeclaration((AnnotationMirror) value.getValue());
			}
		});
	}

	private MethodDeclaration getMethodDeclaration(final AnnotationMirror mirror) {
		return new SithMethodAnnotation(this, mirror).getMethodDeclaration(environment.getMessager());
	}
}
