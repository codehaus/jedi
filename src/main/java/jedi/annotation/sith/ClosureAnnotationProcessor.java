package jedi.annotation.sith;

import static jedi.functional.Coercions.asSet;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.flatten;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jedi.annotation.SithCommand;
import jedi.annotation.SithFilter;
import jedi.annotation.SithFunctor;
import jedi.annotation.SithMethods;
import jedi.annotation.jedi.AbstractClosureAnnotationProcessor;
import jedi.annotation.processor.model.Annotateable;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;

class ClosureAnnotationProcessor extends AbstractClosureAnnotationProcessor {
	public ClosureAnnotationProcessor(final AnnotationProcessorEnvironment environment) {
		super(environment, SithCommand.class, SithFilter.class, SithFunctor.class);
	}

	@Override
	protected Set<Annotateable> getInterestingNonCompositeDeclarations(final AnnotationTypeDeclaration annotationTypeDeclaration) {
		final Set<AnnotationMirror> mirrors = getMirrors(annotationTypeDeclaration);
		return getNonComposites(annotationTypeDeclaration, mirrors);
	}

	@Override
	protected Collection<Annotateable> getInterestingCompositeDeclarations() {
		Set<AnnotationMirror> sithMethodsMirrors = getMirrors(getTypeDeclaration(SithMethods.class));
		final Set<Annotateable> methods = getMethods(sithMethodsMirrors, "commands", SithCommand.class);
		methods.addAll(getMethods(sithMethodsMirrors, "functors", SithFunctor.class));
		methods.addAll(getMethods(sithMethodsMirrors, "filters", SithFilter.class));
		return methods;
	}

	private Set<Annotateable> getMethods(final Set<AnnotationMirror> mirrors, final String property, final Class<?> propertyClass) {
		final AnnotationTypeDeclaration propertyClassTypeDeclaration = getTypeDeclaration(propertyClass);
		return mirrors == null ? //
				Collections.<Annotateable> emptySet() ://
					asSet(flatten(mirrors, new Functor<AnnotationMirror, Collection<Annotateable>>() { //
						@SuppressWarnings("unchecked")
						public Collection<Annotateable> execute(final AnnotationMirror value) {
							return getNonComposites(propertyClassTypeDeclaration,
									getMirrors((List<AnnotationValue>) new AnnotationMirrorInterpreter(value).getValue(property)));
						}
					}));
	}

	private Set<AnnotationMirror> getMirrors(final AnnotationTypeDeclaration annotationTypeDeclaration) {
		return asSet(flatten(environment.getDeclarationsAnnotatedWith(annotationTypeDeclaration),
				new Functor<Declaration, Collection<AnnotationMirror>>() {
			public Collection<AnnotationMirror> execute(final Declaration value) {
				return getMirrors(value, annotationTypeDeclaration);
			}
		}));
	}

	protected List<AnnotationMirror> getMirrors(final List<AnnotationValue> values) {
		return values == null ? Collections.<AnnotationMirror> emptyList() : collect(values,
				new Functor<AnnotationValue, AnnotationMirror>() {
			public AnnotationMirror execute(final AnnotationValue value) {
				return (AnnotationMirror) value.getValue();
			}
		});
	}

	private Set<Annotateable> getNonComposites(final AnnotationTypeDeclaration annotationTypeDeclaration,
			final Collection<AnnotationMirror> mirrors) {
		return asSet(flatten(mirrors, new Functor<AnnotationMirror, Collection<Annotateable>>() {
			public Collection<Annotateable> execute(final AnnotationMirror value) {
				return new SithAnnotation(annotationTypeDeclaration, value, environment)
				.getMethodDeclarations(annotationTypeToFactoryMethodWriterMap.get(annotationTypeDeclaration));
			}
		}));
	}
}
