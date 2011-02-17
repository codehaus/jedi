package jedi.annotation.jedi;

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
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.JediField;
import jedi.annotation.processor.model.JediMethod;
import jedi.annotation.processor5.OptionAccessor5;
import jedi.annotation.processor5.model.AnnotationValueValueFunctor;
import jedi.annotation.sith.AnnotationMirrorInterpreter;
import jedi.filters.NotNullFilter;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.util.DeclarationFilter;

public class ClosureAnnotationProcessor extends AbstractClosureAnnotationProcessor {
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

	public ClosureAnnotationProcessor(AnnotationProcessorEnvironment environment) {
		super(environment, JediCommand.class, JediFilter.class, JediFunctor.class, new OptionAccessor5(environment));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Set<Annotateable> getInterestingNonCompositeDeclarations(final AnnotationTypeDeclaration annotationTypeDeclaration) {
		final Collection<Declaration> annotatedDeclarations = environment.getDeclarationsAnnotatedWith(annotationTypeDeclaration);
		return asSet(append(flatten(INTERESTING_METHOD_FILTER.filter(annotatedDeclarations), new Functor<Declaration, Set<? extends Annotateable>>() {
			public Set<? extends Annotateable> execute(Declaration value) {
				return getRequiredMethods(annotationTypeDeclaration, (MethodDeclaration) value);
			}
		}), flatten(
				INTERESTING_FIELD_FILTER.filter(annotatedDeclarations), new Functor<Declaration, Set<? extends Annotateable>>() {
					public Set<? extends Annotateable> execute(Declaration value) {
						return getRequiredMethods(annotationTypeDeclaration, (FieldDeclaration) value);
					}
				})));
	}

	@Override
	protected Collection<Annotateable> getInterestingCompositeDeclarations() {
		return Collections.<Annotateable>emptySet();
	}

	private Set<? extends Annotateable> getRequiredMethods(AnnotationTypeDeclaration annotationTypeDeclaration, FieldDeclaration field) {
		AnnotationMirrorInterpreter interpreter = new AnnotationMirrorInterpreter(head(getMirrors(field, annotationTypeDeclaration)));
		String factoryPrefix = (String) interpreter.getValue("name");
		if (factoryPrefix == null) {
			factoryPrefix = field.getSimpleName();
		}
		return set(new JediField(new jedi.annotation.processor5.model.FieldDeclarationAdapter(field), annotationClassToFactoryMethodWriterMap.get(annotationTypeDeclaration), factoryPrefix));
	}

	@SuppressWarnings("unchecked")
	private Set<? extends Annotateable> getRequiredMethods(final AnnotationTypeDeclaration annotationTypeDeclaration, MethodDeclaration method) {
		AnnotationMirrorInterpreter interpreter = new AnnotationMirrorInterpreter(head(getMirrors(method, annotationTypeDeclaration)));
		String factoryPrefix = (String) interpreter.getValue("name");
		if (factoryPrefix == null) {
			factoryPrefix = method.getSimpleName();
		}

		List<AnnotationValue> value = (List<AnnotationValue>) interpreter.getValue("cut");
		return value != null ? getCuts(annotationTypeDeclaration, method, value, factoryPrefix) : set(new JediMethod(new jedi.annotation.processor5.model.MethodDeclarationAdapter(method),
				annotationClassToFactoryMethodWriterMap.get(annotationTypeDeclaration), factoryPrefix));
	}

	private Set<Annotateable> getCuts(final AnnotationTypeDeclaration annotationTypeDeclaration, final MethodDeclaration method,
			List<AnnotationValue> cuts, final String factoryPrefix) {
		return asSet(select(collect(cuts, new Functor<AnnotationValue, Annotateable>() {
			public Annotateable execute(AnnotationValue value) {
				return createCutMethod(annotationTypeDeclaration, method, ((AnnotationMirror) value.getValue()), factoryPrefix);
			}
		}), new NotNullFilter<Annotateable>()));
	}

	@SuppressWarnings("unchecked")
	private Annotateable createCutMethod(AnnotationTypeDeclaration annotationTypeDeclaration, MethodDeclaration method, AnnotationMirror cut, String factoryPrefix) {
		AnnotationMirrorInterpreter interpreter = new AnnotationMirrorInterpreter(cut);

		String name = (String) interpreter.getValue("name");
		if (name == null) {
			name = factoryPrefix;
		}
		List<String> parameterNames = getCutParameterNames((List<AnnotationValue>) interpreter.getValue("parameters"));

		return validateCutParameters(method, parameterNames) ? new JediMethod(new jedi.annotation.processor5.model.MethodDeclarationAdapter(method), annotationClassToFactoryMethodWriterMap
				.get(annotationTypeDeclaration), name, asSet(parameterNames)) : null;

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
			public String execute(ParameterDeclaration value) {
				return value.getSimpleName();
			}
		});
	}

	private List<String> getCutParameterNames(List<AnnotationValue> values) {
		return values == null ? Collections.<String> emptyList() : collect(values, new AnnotationValueValueFunctor<String>());
	}
}
