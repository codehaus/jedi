package jedi.annotation.sith;

import static jedi.functional.Coercions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jedi.annotation.SithCommand;
import jedi.annotation.SithFilter;
import jedi.annotation.SithFunctor;
import jedi.annotation.SithMethods;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class ProcessorFactory implements AnnotationProcessorFactory {
    public AnnotationProcessor getProcessorFor(final Set<AnnotationTypeDeclaration> typeDeclarations, final AnnotationProcessorEnvironment environment) {
        return typeDeclarations.isEmpty() ? AnnotationProcessors.NO_OP : new ClosureAnnotationProcessor(environment);
    }

    public Collection<String> supportedAnnotationTypes() {
        return set(SithMethods.class.getName(), SithCommand.class.getName(), SithFilter.class.getName(), SithFunctor.class.getName());
    }

    public Collection<String> supportedOptions() {
        return Collections.emptySet();
    }
}