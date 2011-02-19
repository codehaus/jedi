package jedi.annotation.processor5;

import static jedi.functional.Coercions.asList;
import static jedi.functional.Coercions.asSet;
import static jedi.functional.Coercions.set;
import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.flatten;
import static jedi.functional.FunctionalPrimitives.head;
import static jedi.functional.FunctionalPrimitives.select;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jedi.annotation.JediCommand;
import jedi.annotation.JediFilter;
import jedi.annotation.JediFunctor;
import jedi.annotation.processor.AnnotatedMemberDeclarationProcessor;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.JediField;
import jedi.annotation.processor.model.JediMethod;
import jedi.annotation.processor5.model.AnnotationMirrorInterpreter;
import jedi.annotation.processor5.model.AnnotationValueValueFunctor;
import jedi.filters.NotNullFilter;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.util.DeclarationFilter;

public class JediAnnotationProcessor implements AnnotationProcessor {
	private static final DeclarationFilter MEMBER_VISIBILITY_FILTER = //
		DeclarationFilter.FILTER_PUBLIC //
		.or(DeclarationFilter.FILTER_PACKAGE) //
		.or(DeclarationFilter.FILTER_PROTECTED);

	private static final DeclarationFilter INTERESTING_METHOD_FILTER = new DeclarationFilter() {
		@Override
		public boolean matches(Declaration declaration) {
			return declaration instanceof MethodDeclaration;
		}
	}.and(MEMBER_VISIBILITY_FILTER);

	private static final DeclarationFilter INTERESTING_FIELD_FILTER = new DeclarationFilter() {
		@Override
		public boolean matches(Declaration declaration) {
			return declaration instanceof FieldDeclaration;
		}
	}.and(MEMBER_VISIBILITY_FILTER);

	private final AnnotationProcessorEnvironment environment;

	public JediAnnotationProcessor(AnnotationProcessorEnvironment environment) {
		this.environment = environment;
	}

	@Override
	public void process() {
		new AnnotatedMemberDeclarationProcessor(JediCommand.class, JediFilter.class, JediFunctor.class, new OptionAccessor5(environment), new Environment5(environment))
		.process(asSet(append(
				getAnnotateMemberDeclarations(JediCommand.class),
				getAnnotateMemberDeclarations(JediFilter.class),
				getAnnotateMemberDeclarations(JediFunctor.class))));
	}

	@SuppressWarnings("unchecked")
	private Set<Annotateable> getAnnotateMemberDeclarations(final Class<?> annotationClass) {
		final Collection<Declaration> annotatedDeclarations = environment.getDeclarationsAnnotatedWith(EnvironmentUtils.getTypeDeclaration(environment, annotationClass));
		return asSet(append(
				flatten(INTERESTING_METHOD_FILTER.filter(annotatedDeclarations), new Functor<Declaration, Set<? extends Annotateable>>() {
					public Set<? extends Annotateable> execute(Declaration value) {
						return getRequiredMethods(annotationClass, (MethodDeclaration) value);
					}
				}),
				flatten(INTERESTING_FIELD_FILTER.filter(annotatedDeclarations), new Functor<Declaration, Set<? extends Annotateable>>() {
					public Set<? extends Annotateable> execute(Declaration value) {
						return getRequiredMethods(annotationClass, (FieldDeclaration) value);
					}
				})));
	}

	private Set<? extends Annotateable> getRequiredMethods(Class<?> annotationClass, FieldDeclaration field) {
		AnnotationMirrorInterpreter interpreter = new AnnotationMirrorInterpreter(head(EnvironmentUtils.getMirrors(environment, field, annotationClass)));
		String factoryPrefix = (String) interpreter.getValue("name");
		if (factoryPrefix == null) {
			factoryPrefix = field.getSimpleName();
		}
		return set(new JediField(new jedi.annotation.processor5.model.FieldDeclarationAdapter(field), annotationClass, factoryPrefix));
	}

	@SuppressWarnings("unchecked")
	private Set<? extends Annotateable> getRequiredMethods(final Class<?> annotationClass, MethodDeclaration method) {
		AnnotationMirrorInterpreter interpreter = new AnnotationMirrorInterpreter(head(EnvironmentUtils.getMirrors(environment, method, annotationClass)));
		String factoryPrefix = (String) interpreter.getValue("name");
		if (factoryPrefix == null) {
			factoryPrefix = method.getSimpleName();
		}

		List<AnnotationValue> value = (List<AnnotationValue>) interpreter.getValue("cut");
		return value != null ? getCuts(annotationClass, method, value, factoryPrefix) : set(new JediMethod(new jedi.annotation.processor5.model.MethodDeclarationAdapter(method), annotationClass, factoryPrefix));
	}

	private Set<Annotateable> getCuts(final Class<?> annotationClass, final MethodDeclaration method, List<AnnotationValue> cuts,
			final String factoryPrefix) {
		return asSet(select(collect(cuts, new Functor<AnnotationValue, Annotateable>() {
			public Annotateable execute(AnnotationValue value) {
				return createCutMethod(annotationClass, method, ((AnnotationMirror) value.getValue()), factoryPrefix);
			}
		}), new NotNullFilter<Annotateable>()));
	}

	@SuppressWarnings("unchecked")
	private Annotateable createCutMethod(Class<?> annotationClass, MethodDeclaration method, AnnotationMirror cut, String factoryPrefix) {
		AnnotationMirrorInterpreter interpreter = new AnnotationMirrorInterpreter(cut);

		String name = (String) interpreter.getValue("name");
		if (name == null) {
			name = factoryPrefix;
		}
		List<String> parameterNames = getCutParameterNames((List<AnnotationValue>) interpreter.getValue("parameters"));

		return validateCutParameters(method, parameterNames) ? new JediMethod(new jedi.annotation.processor5.model.MethodDeclarationAdapter(method), annotationClass, name, asSet(parameterNames)) : null;
	}

	private boolean validateCutParameters(MethodDeclaration method, List<String> parameterNames) {
		List<String> outstanding = asList(parameterNames);
		outstanding.removeAll(getNames(method.getParameters()));
		if (outstanding.isEmpty()) {
			return true;
		}

		environment.getMessager().printError(method.getPosition(), "Cut parameters do not exist in formal parameter list: " + outstanding);
		return false;
	}

	private List<String> getNames(Collection<ParameterDeclaration> parameters) {
		return collect(parameters, new Functor<ParameterDeclaration, String>() {
			@Override
			public String execute(ParameterDeclaration value) {
				return value.getSimpleName();
			}
		});
	}

	private List<String> getCutParameterNames(List<AnnotationValue> values) {
		return values == null ? Collections.<String> emptyList() : collect(values, new AnnotationValueValueFunctor<String>());
	}
}
