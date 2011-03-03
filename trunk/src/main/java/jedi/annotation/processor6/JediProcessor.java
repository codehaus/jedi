package jedi.annotation.processor6;

import static jedi.functional.Coercions.asSet;
import static jedi.functional.Coercions.set;
import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.flatten;
import static jedi.functional.FunctionalPrimitives.select;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

import jedi.annotation.JediCommand;
import jedi.annotation.JediCut;
import jedi.annotation.JediFilter;
import jedi.annotation.JediFunctor;
import jedi.annotation.processor.AnnotatedMemberDeclarationProcessor;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.JediField;
import jedi.annotation.processor.model.JediMethod;
import jedi.annotation.processor6.model.BoxerFunctor;
import jedi.annotation.processor6.model.FieldDeclarationAdapter;
import jedi.annotation.processor6.model.MethodDeclarationAdapter;
import jedi.filters.NotNullFilter;
import jedi.functional.Filter;
import jedi.functional.Functor;

@SupportedAnnotationTypes(value = { "jedi.annotation.JediFunctor", "jedi.annotation.JediFilter", "jedi.annotation.JediCommand" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JediProcessor extends AbstractProcessor {
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment environment) {
		new AnnotatedMemberDeclarationProcessor(JediCommand.class, JediFilter.class, JediFunctor.class, new OptionAccessor6(processingEnv), new Environment6(processingEnv))
		.process(asSet(append(
				getAnnotatedMemberDeclarations(environment, JediCommand.class),
				getAnnotatedMemberDeclarations(environment, JediFunctor.class),
				getAnnotatedMemberDeclarations(environment, JediFilter.class))));

		return true;
	}

	@SuppressWarnings("unchecked")
	private Set<Annotateable> getAnnotatedMemberDeclarations(RoundEnvironment environment, final Class<? extends Annotation> annotationClass) {
		final Set<? extends Element> annotatedElements = environment.getElementsAnnotatedWith(annotationClass);
		return asSet(append(
				createJediMethods(selectAndCast(annotatedElements, ExecutableElement.class), annotationClass),
				createJediFields(selectAndCast(annotatedElements, VariableElement.class), annotationClass)));
	}

	private List<Annotateable> createJediFields(List<VariableElement> annotatedFields, final Class<? extends Annotation> annotationClass) {
		return collect(annotatedFields, new Functor<VariableElement, Annotateable>() {
			@Override
			public Annotateable execute(VariableElement value) {
				return getAnnotateable(value, annotationClass);
			}
		});
	}

	private List<Annotateable> createJediMethods(List<ExecutableElement> annotatedMethods, final Class<? extends Annotation> annotationClass) {
		return flatten(annotatedMethods, new Functor<ExecutableElement, Set<? extends Annotateable>>() {
			@Override
			public Set<? extends Annotateable> execute(ExecutableElement value) {
				return getAnnotateables(value, annotationClass);
			}
		});
	}

	protected Annotateable getAnnotateable(VariableElement field, Class<? extends Annotation> annotationClass) {
		return new JediField(new FieldDeclarationAdapter(new BoxerFunctor(processingEnv.getTypeUtils()), field), annotationClass, getName(field, annotationClass, field.getSimpleName().toString()));
	}

	private String getName(Element element, Class<? extends Annotation> annotationClass, String defaultName) {
		Annotation a = element.getAnnotation(annotationClass);
		String name = null;
		if (a instanceof JediCommand) {
			name = ((JediCommand) a).name();
		} else if (a instanceof JediFilter) {
			name = ((JediFilter) a).name();
		} else if (a instanceof JediFunctor) {
			name = ((JediFunctor) a).name();
		}
		return name == null ? defaultName : name;
	}

	protected Set<? extends Annotateable> getAnnotateables(ExecutableElement method, Class<? extends Annotation> annotationClass) {
		String name = getName(method, annotationClass, method.getSimpleName().toString());
		JediCut[] cuts = getCuts(method, annotationClass);
		return cuts == null ? set(new JediMethod(new MethodDeclarationAdapter(new BoxerFunctor(processingEnv.getTypeUtils()), method), annotationClass, name)) : createCutMethods(method, annotationClass, cuts, name);
	}

	private Set<JediMethod> createCutMethods(final ExecutableElement method, final Class<? extends Annotation> annotationClass, final JediCut[] cuts, final String name) {
		return asSet(select(collect(cuts, new Functor<JediCut, JediMethod>() {
			@Override
			public JediMethod execute(JediCut value) {
				return createCutMethod(method, annotationClass, value, name);
			}
		}), new NotNullFilter<Annotateable>()));
	}

	private JediMethod createCutMethod(ExecutableElement method, Class<? extends Annotation> annotationClass, JediCut cut, String name) {
		String cutName = cut.name() == null ? name : cut.name();
		Set<String> parameters = asSet(cut.parameters());
		return validateCutParameters(method, parameters) ? new JediMethod(new MethodDeclarationAdapter(new BoxerFunctor(processingEnv.getTypeUtils()), method), annotationClass, cutName, parameters) : null;
	}

	private boolean validateCutParameters(ExecutableElement method, Set<String> parameters) {
		Set<String> outstanding = asSet(parameters);
		outstanding.removeAll(collect(method.getParameters(), new Functor<VariableElement, String>() {
			@Override
			public String execute(VariableElement value) {
				return value.getSimpleName().toString();
			}
		}));
		if (outstanding.isEmpty()) {
			return true;
		}

		processingEnv.getMessager().printMessage(Kind.ERROR, "Cut parameters do not exist in formal parameter list: " + outstanding, method);
		return false;
	}

	private JediCut[] getCuts(ExecutableElement method, Class<? extends Annotation> annotationClass) {
		Annotation a = method.getAnnotation(annotationClass);
		JediCut[] cuts = null;
		if (a instanceof JediCommand) {
			cuts = ((JediCommand) a).cut();
		} else if (a instanceof JediFilter) {
			cuts = ((JediFilter) a).cut();
		} else if (a instanceof JediFunctor) {
			cuts = ((JediFunctor) a).cut();
		}
		return cuts == null || cuts.length == 0 ? null : cuts;
	}

	@SuppressWarnings("unchecked")
	private <T, R> List<R> selectAndCast(Iterable<T> iterable, final Class<R> r) {
		return (List<R>) select(iterable, new Filter<Object>() {
			@Override
			public Boolean execute(Object value) {
				return value != null && r.isAssignableFrom(value.getClass());
			}
		});
	}
}