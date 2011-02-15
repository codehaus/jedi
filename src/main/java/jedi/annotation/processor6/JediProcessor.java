package jedi.annotation.processor6;

import static jedi.functional.FunctionalPrimitives.group;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import jedi.functional.Functor;

@SupportedAnnotationTypes(value = { "jedi.annotation.JediFunctor", "jedi.annotation.JediFilter", "jedi.annotation.JediCommand" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JediProcessor extends AbstractProcessor {
	@Override
	public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
		try {
			writeFactories(groupElementsByType(getAnnotatedElements(typeElements, roundEnvironment)));
		} catch (IOException e) {
			this.processingEnv.getMessager().printMessage(Kind.ERROR, e.getMessage());
			return false;
		}
		return true;
	}

	private void writeFactories(final Map<TypeElement, List<Element>> elementsByType) throws IOException {
		for (Map.Entry<TypeElement, List<Element>> entry : elementsByType.entrySet()) {
			write(entry.getKey(), entry.getValue());
		}
	}

	private void write(TypeElement key, List<Element> value) throws IOException {

	}

	private Map<TypeElement, List<Element>> groupElementsByType(final Set<Element> elements) {
		return group(elements, new Functor<Element, TypeElement>() {
			@Override
			public TypeElement execute(Element value) {
				return (value.getKind() == ElementKind.CLASS || value.getKind() == ElementKind.INTERFACE) ? (TypeElement) value : execute(value.getEnclosingElement());
			}
		});
	}

	private Set<Element> getAnnotatedElements(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
		Set<Element> elements = new HashSet<Element>();
		for (TypeElement type : typeElements) {
			elements.addAll(roundEnvironment.getElementsAnnotatedWith(type));
		}
		return elements;
	}
}