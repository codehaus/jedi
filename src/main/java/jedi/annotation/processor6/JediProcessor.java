package jedi.annotation.processor6;

import static jedi.functional.Coercions.asSet;
import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.map;

import java.lang.annotation.Annotation;
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

import jedi.annotation.JediCommand;
import jedi.annotation.JediFilter;
import jedi.annotation.JediFunctor;
import jedi.annotation.processor.AnnotatedMemberDeclarationProcessor;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.JediField;
import jedi.annotation.processor.model.JediMethod;
import jedi.annotation.processor6.model.FieldDeclarationAdapter;
import jedi.annotation.processor6.model.MethodDeclarationAdapter;
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

	private Set<Annotateable> getAnnotatedMemberDeclarations(RoundEnvironment environment, final Class<? extends Annotation> annotationClass) {
		return map((Set<? extends Element>) getAnnotatedElements(environment, annotationClass), new Functor<Element, Annotateable>() {
			@Override
			public Annotateable execute(Element value) {
				return (value instanceof ExecutableElement) ? new JediMethod(new MethodDeclarationAdapter((ExecutableElement) value), annotationClass) : new JediField(new FieldDeclarationAdapter((VariableElement) value), annotationClass, value.getSimpleName().toString());
			}
		});
	}

	private Set<? extends Element> getAnnotatedElements(RoundEnvironment roundEnvironment, Class<? extends Annotation> annotationClass) {
		return roundEnvironment.getElementsAnnotatedWith(annotationClass);
	}
}