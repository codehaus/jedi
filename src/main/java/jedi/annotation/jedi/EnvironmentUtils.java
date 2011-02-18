package jedi.annotation.jedi;

import static jedi.functional.FunctionalPrimitives.select;

import java.util.Collection;

import jedi.functional.Filter;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;

public class EnvironmentUtils {
	public static Collection<AnnotationMirror> getMirrors(final AnnotationProcessorEnvironment environment, final Declaration declaration, final Class<?> annotationClass) {
		return select(declaration.getAnnotationMirrors(), new Filter<AnnotationMirror>() {
			@Override
			public Boolean execute(final AnnotationMirror mirror) {
				return mirror.getAnnotationType().getDeclaration().equals(getTypeDeclaration(environment, annotationClass));
			}
		});
	}

	public static AnnotationTypeDeclaration getTypeDeclaration(final AnnotationProcessorEnvironment environment, final Class<?> annotationClass) {
		return (AnnotationTypeDeclaration) environment.getTypeDeclaration(annotationClass.getName());
	}


}
