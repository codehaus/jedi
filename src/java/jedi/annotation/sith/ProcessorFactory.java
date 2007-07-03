package jedi.annotation.sith;

import static jedi.functional.Coercions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jedi.annotation.SithCommand;
import jedi.annotation.SithFilter;
import jedi.annotation.SithFunctor;
import jedi.annotation.SithMethod;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class ProcessorFactory implements AnnotationProcessorFactory {
    public Collection<String> supportedOptions() {
        return Collections.emptySet();
    }
    
    public Collection<String> supportedAnnotationTypes() {
        return set(SithCommand.class.getName(), SithFilter.class.getName(), SithFunctor.class.getName(), SithMethod.class.getName());
    }
    
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> typeDeclarations, AnnotationProcessorEnvironment environment) {
        return typeDeclarations.isEmpty() ? AnnotationProcessors.NO_OP : new ClosureAnnotationProcessor(environment);
    }
}
