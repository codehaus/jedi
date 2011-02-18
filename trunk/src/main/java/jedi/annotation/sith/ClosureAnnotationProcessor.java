package jedi.annotation.sith;

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
import jedi.annotation.jedi.AbstractClosureAnnotationProcessor;
import jedi.annotation.jedi.EnvironmentUtils;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor5.Environment5;
import jedi.annotation.processor5.OptionAccessor5;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;

class ClosureAnnotationProcessor implements AnnotationProcessor {
	private final AnnotationProcessorEnvironment environment;

	public ClosureAnnotationProcessor(final AnnotationProcessorEnvironment environment) {
		this.environment = environment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process() {
		Set<AnnotationMirror> sithMethodsMirrors = getMirrors(SithMethods.class);
		new AbstractClosureAnnotationProcessor(SithCommand.class, SithFilter.class, SithFunctor.class, new OptionAccessor5(environment), new Environment5(environment))
		.process(asSet(append(
				getNonComposites(SithCommand.class, getMirrors(SithCommand.class)),
				getNonComposites(SithFilter.class, getMirrors(SithFilter.class)),
				getNonComposites(SithFunctor.class, getMirrors(SithFunctor.class)),
				getMethods(sithMethodsMirrors, "commands", SithCommand.class),
				getMethods(sithMethodsMirrors, "functors", SithFunctor.class),
				getMethods(sithMethodsMirrors, "filters", SithFilter.class))));
	}

	private Set<Annotateable> getMethods(final Set<AnnotationMirror> mirrors, final String property, final Class<?> propertyClass) {
		return mirrors == null ? //
				Collections.<Annotateable> emptySet()
				: //
					asSet(flatten(mirrors, new Functor<AnnotationMirror, Collection<Annotateable>>() { //
						@Override
						@SuppressWarnings("unchecked")
						public Collection<Annotateable> execute(final AnnotationMirror value) {
							return getNonComposites(propertyClass, getMirrors((List<AnnotationValue>) new AnnotationMirrorInterpreter(
									value).getValue(property)));
						}
					}));
	}

	private Set<AnnotationMirror> getMirrors(final Class<?> annotationClass) {
		return asSet(flatten(environment.getDeclarationsAnnotatedWith(EnvironmentUtils.getTypeDeclaration(environment, annotationClass)),
				new Functor<Declaration, Collection<AnnotationMirror>>() {
			@Override
			public Collection<AnnotationMirror> execute(final Declaration value) {
				return EnvironmentUtils.getMirrors(environment, value, annotationClass);
			}
		}));
	}

	protected List<AnnotationMirror> getMirrors(final List<AnnotationValue> values) {
		return values == null ? Collections.<AnnotationMirror> emptyList() : collect(values,
				new Functor<AnnotationValue, AnnotationMirror>() {
			@Override
			public AnnotationMirror execute(final AnnotationValue value) {
				return (AnnotationMirror) value.getValue();
			}
		});
	}

	private Set<Annotateable> getNonComposites(final Class<?> annotationClass, final Collection<AnnotationMirror> mirrors) {
		return asSet(flatten(mirrors, new Functor<AnnotationMirror, Collection<Annotateable>>() {
			@Override
			public Collection<Annotateable> execute(final AnnotationMirror value) {
				return new SithAnnotation(EnvironmentUtils.getTypeDeclaration(environment, annotationClass), value, environment).getMethodDeclarations(annotationClass);
			}
		}));
	}
}
