package jedi.annotation.processor5;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jedi.annotation.JediCommand;
import jedi.annotation.JediCut;
import jedi.annotation.JediFilter;
import jedi.annotation.JediFunctor;
import jedi.functional.Coercions;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class JediProcessorFactory implements AnnotationProcessorFactory {
	public Collection<String> supportedOptions() {
		return Collections.emptySet();
	}

	public Collection<String> supportedAnnotationTypes() {
		return Coercions.set(JediCommand.class.getName(), JediFilter.class.getName(), JediFunctor.class.getName(), JediCut.class.getName());
	}

	public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> typeDeclarations, AnnotationProcessorEnvironment environment) {
		return typeDeclarations.isEmpty() ? AnnotationProcessors.NO_OP : new JediAnnotationProcessor(environment);
	}
}
