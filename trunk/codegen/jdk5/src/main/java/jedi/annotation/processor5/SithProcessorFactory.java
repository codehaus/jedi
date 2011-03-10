package jedi.annotation.processor5;

import static jedi.functional.Coercions.set;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jedi.annotation.SithCommand;
import jedi.annotation.SithFilter;
import jedi.annotation.SithFunctor;
import jedi.annotation.SithMethod;
import jedi.annotation.SithMethods;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class SithProcessorFactory implements AnnotationProcessorFactory {
	public AnnotationProcessor getProcessorFor(final Set<AnnotationTypeDeclaration> typeDeclarations, final AnnotationProcessorEnvironment environment) {
		return typeDeclarations.isEmpty() ? AnnotationProcessors.NO_OP : new SithAnnotationProcessor(environment);
	}

	public Collection<String> supportedAnnotationTypes() {
		return set(SithMethods.class.getName(), SithCommand.class.getName(), SithFilter.class.getName(), SithFunctor.class.getName(), SithMethod.class.getName());
	}

	public Collection<String> supportedOptions() {
		return Collections.emptySet();
	}
}
