package jedi.annotation.processor5;

import static jedi.functional.Coercions.asSet;
import static jedi.functional.FunctionalPrimitives.append;
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
import jedi.annotation.processor.AnnotatedMemberDeclarationProcessor;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor5.model.AnnotationMirrorInterpreter;
import jedi.annotation.processor5.model.SithAnnotation;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;

class SithAnnotationProcessor implements AnnotationProcessor {
	private final AnnotationProcessorEnvironment environment;

	public SithAnnotationProcessor(final AnnotationProcessorEnvironment environment) {
		this.environment = environment;
	}

	@SuppressWarnings("unchecked")
	public void process() {
		Set<AnnotationMirror> mirrors = getMirrors(SithMethods.class);
		new AnnotatedMemberDeclarationProcessor(SithCommand.class, SithFilter.class, SithFunctor.class, new OptionAccessor5(environment), new Environment5(environment))
		.process(asSet(append(
				getSimpleAnnotatedMethods(SithCommand.class, getMirrors(SithCommand.class)),
				getSimpleAnnotatedMethods(SithFilter.class, getMirrors(SithFilter.class)),
				getSimpleAnnotatedMethods(SithFunctor.class, getMirrors(SithFunctor.class)),
				getSimplifiedCompositeAnnotatedMethods(mirrors, "commands", SithCommand.class),
				getSimplifiedCompositeAnnotatedMethods(mirrors, "functors", SithFunctor.class),
				getSimplifiedCompositeAnnotatedMethods(mirrors, "filters", SithFilter.class))));
	}

	private Set<Annotateable> getSimplifiedCompositeAnnotatedMethods(final Set<AnnotationMirror> mirrors, final String property, final Class<?> propertyClass) {
		return mirrors == null ? //
				Collections.<Annotateable> emptySet()
				: //
					asSet(flatten(mirrors, new Functor<AnnotationMirror, Collection<Annotateable>>() { //
						@SuppressWarnings("unchecked")
						public Collection<Annotateable> execute(final AnnotationMirror value) {
							return getSimpleAnnotatedMethods(propertyClass, getMirrors((List<AnnotationValue>) new AnnotationMirrorInterpreter(value).getValue(property)));
						}
					}));
	}

	private Set<AnnotationMirror> getMirrors(final Class<?> annotationClass) {
		return asSet(flatten(environment.getDeclarationsAnnotatedWith(EnvironmentUtils.getTypeDeclaration(environment, annotationClass)),
				new Functor<Declaration, Collection<AnnotationMirror>>() {
			public Collection<AnnotationMirror> execute(final Declaration value) {
				return EnvironmentUtils.getMirrors(environment, value, annotationClass);
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

	private Set<Annotateable> getSimpleAnnotatedMethods(final Class<?> annotationClass, final Collection<AnnotationMirror> mirrors) {
		return asSet(flatten(mirrors, new Functor<AnnotationMirror, Collection<Annotateable>>() {
			public Collection<Annotateable> execute(final AnnotationMirror value) {
				return new SithAnnotation(EnvironmentUtils.getTypeDeclaration(environment, annotationClass), value, environment).getMethodDeclarations(annotationClass);
			}
		}));
	}
}
