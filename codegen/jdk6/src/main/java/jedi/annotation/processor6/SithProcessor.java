package jedi.annotation.processor6;

import static jedi.functional.Coercions.asSet;
import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.flatMap;
import static jedi.functional.FunctionalPrimitives.flatten;
import static jedi.functional.FunctionalPrimitives.head;
import static jedi.functional.FunctionalPrimitives.headOrNullIfEmpty;
import static jedi.functional.FunctionalPrimitives.select;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import jedi.annotation.SithCommand;
import jedi.annotation.SithFilter;
import jedi.annotation.SithFunctor;
import jedi.annotation.SithMethods;
import jedi.annotation.processor.AnnotatedMemberDeclarationProcessor;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.JediMethod;
import jedi.annotation.processor6.model.BoxerFunctor;
import jedi.annotation.processor6.model.MethodDeclarationAdapter;
import jedi.filters.NotNullFilter;
import jedi.functional.Filter;
import jedi.functional.Functor;

@SupportedAnnotationTypes(value = { "jedi.annotation.SithFunctor", "jedi.annotation.SithFilter", "jedi.annotation.SithCommand", "jedi.annotation.SithMethods"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SithProcessor extends AbstractProcessor {
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment environment) {
		Set<? extends Element> composites = environment.getElementsAnnotatedWith(SithMethods.class);
		new AnnotatedMemberDeclarationProcessor(SithCommand.class, SithFilter.class, SithFunctor.class, new OptionAccessor6(processingEnv), new Environment6(processingEnv))
		.process(asSet(append(
				getMethods(environment, SithCommand.class),
				getMethods(environment, SithFilter.class),
				getMethods(environment, SithFunctor.class),
				getSimplifiedCompositeMethods(environment, composites, "commands", SithCommand.class),
				getSimplifiedCompositeMethods(environment, composites, "filters", SithFilter.class),
				getSimplifiedCompositeMethods(environment, composites, "functors", SithFunctor.class))));

		return true;
	}

	private Collection<? extends Annotateable> getSimplifiedCompositeMethods(final RoundEnvironment environment, final Set<? extends Element> composites, final String property, final Class<? extends Annotation> annotationClass) {
		return asSet(flatMap(composites, new Functor<Element, Collection<Annotateable>>() {
			@Override
			public Collection<Annotateable> execute(Element element) {
				AnnotationMirror sithMethodsMirror = getRequiredAnnotationMirror(element, SithMethods.class);
				@SuppressWarnings("unchecked")
				List<AnnotationValue> closures = (List<AnnotationValue>) getValue(sithMethodsMirror, property);
				return getMethods(element, annotationClass, getAnnotationMirrors(closures));
			}
		}));
	}

	protected Collection<Annotateable> getMethods(final Element element, final Class<? extends Annotation> annotationClass, List<AnnotationMirror> annotationMirrors) {
		return flatMap(annotationMirrors, new Functor<AnnotationMirror, List<Annotateable>>() {
			@Override
			public List<Annotateable> execute(AnnotationMirror mirror) {
				return getMethods(element, annotationClass, mirror);
			}
		});
	}

	private List<Annotateable> getMethods(RoundEnvironment environment, final Class<? extends Annotation> annotationClass) {
		final Set<? extends Element> annotatedElements = environment.getElementsAnnotatedWith(annotationClass);
		return flatten(annotatedElements, new Functor<Element, List<Annotateable>>() {
			@Override
			public List<Annotateable> execute(Element element) {
				return getMethods(element, annotationClass);
			}
		});
	}

	private List<Annotateable> getMethods(final Element element, final Class<? extends Annotation> annotationClass) {
		return getMethods(element, annotationClass, getRequiredAnnotationMirror(element, annotationClass));
	}

	private List<Annotateable> getMethods(final Element element, final Class<? extends Annotation> annotationClass, AnnotationMirror annotation) {
		final TypeMirror type = (TypeMirror) getValue(annotation, "type");
		final List<AnnotationMirror> methods = getAnnotationMirrors((List<AnnotationValue>) getValue(annotation, "methods"));
		return getMethods(element, type, methods, annotationClass);
	}

	private List<AnnotationMirror> getAnnotationMirrors(List<AnnotationValue> annotationValues) {
		return collect(annotationValues, new Functor<AnnotationValue, AnnotationMirror>() {
			@Override
			public AnnotationMirror execute(AnnotationValue value) {
				return (AnnotationMirror) value.getValue();
			}
		});
	}

	private List<Annotateable> getMethods(final Element element, final TypeMirror type, final List<AnnotationMirror> sithMethods, final Class<?> annotationClass) {
		return select(
				collect(sithMethods, new Functor<AnnotationMirror, Annotateable>() {
					@Override
					public Annotateable execute(AnnotationMirror sithMethod) {
						return getMethod(element, type, sithMethod, annotationClass);
					}
				}),
				new NotNullFilter<Annotateable>());
	}

	protected JediMethod getMethod(Element element, TypeMirror type, AnnotationMirror sithMethod, Class<?> annotationClass) {
		final String factoryName = (String) getValue(sithMethod, "factoryName");
		final String name = (String) getValue(sithMethod, "name");
		final List<TypeMirror> parameterTypes = collect((List<AnnotationValue>) getValue(sithMethod, "parameterTypes"), new Functor<AnnotationValue, TypeMirror>() {
			@Override
			public TypeMirror execute(AnnotationValue value) {
				return (TypeMirror) value.getValue();
			}
		});

		final Types types = processingEnv.getTypeUtils();
		TypeElement typeElement = (TypeElement) types.asElement(type);
		ExecutableElement method = findMethod(typeElement, name, parameterTypes);
		if (method == null) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "No such method", element, sithMethod);
			return null;
		}

		final MethodDeclarationAdapter methodDeclarationAdapter = new MethodDeclarationAdapter(new BoxerFunctor(types), method);
		return new JediMethod(methodDeclarationAdapter, annotationClass, factoryName == null || factoryName.equals("") ? name : factoryName);
	}

	private ExecutableElement findMethod(final TypeElement typeElement, final String name, final List<TypeMirror> parameterTypes) {
		return (ExecutableElement) headOrNullIfEmpty((List<? extends Element>) select(typeElement.getEnclosedElements(), new Filter<Element>() {
			@Override
			public Boolean execute(Element value) {
				return value instanceof ExecutableElement && isMatchingMethod((ExecutableElement) value, name, parameterTypes);
			}
		}));
	}

	private boolean isMatchingMethod(ExecutableElement method, String name, List<TypeMirror> parameterTypes) {
		if (!method.getSimpleName().toString().equals(name)) {
			return false;
		}

		if (method.getParameters().size() != parameterTypes.size()) {
			return false;
		}

		for (int i = 0 ; i < parameterTypes.size() ; i++) {
			if (!processingEnv.getTypeUtils().isSameType(method.getParameters().get(i).asType(), parameterTypes.get(i))) {
				return false;
			}
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private Object getValue(AnnotationMirror mirror, final String key) {
		final List<?> value = select(getElementValuesWithDefaults(mirror).entrySet(), new Filter<Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>>() {
			@Override
			public Boolean execute(Entry<? extends ExecutableElement, ? extends AnnotationValue> value) {
				return key.equals(value.getKey().getSimpleName().toString());
			}
		});
		return value.isEmpty() ? null : ((Entry<ExecutableElement, AnnotationValue>) head(value)).getValue().getValue();
	}

	private Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(AnnotationMirror mirror) {
		return processingEnv.getElementUtils().getElementValuesWithDefaults(mirror);
	}

	private AnnotationMirror getRequiredAnnotationMirror(final Element element, final Class<? extends Annotation> annotationClass) {
		AnnotationMirror mirror = head(select(element.getAnnotationMirrors(), new Filter<AnnotationMirror>() {
			@Override
			public Boolean execute(AnnotationMirror mirror) {
				return mirror.getAnnotationType().toString().equals(annotationClass.getName());
			}
		}));
		return mirror;
	}
}