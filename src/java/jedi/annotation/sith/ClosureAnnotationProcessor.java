package jedi.annotation.sith;

import static jedi.functional.Coercions.*;
import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.Set;

import jedi.annotation.SithCommand;
import jedi.annotation.SithFilter;
import jedi.annotation.SithFunctor;
import jedi.annotation.jedi.AbstractClosureAnnotationProcessor;
import jedi.annotation.jedi.JediMethod;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;

class ClosureAnnotationProcessor extends AbstractClosureAnnotationProcessor {
    public ClosureAnnotationProcessor(AnnotationProcessorEnvironment environment) {
        super(environment, SithCommand.class, SithFilter.class, SithFunctor.class);
    }

    @Override
    protected Set<JediMethod> getInterestingDeclarations(final AnnotationTypeDeclaration annotationTypeDeclaration) {
        return asSet(flatten(
                getMirrors(annotationTypeDeclaration), new Functor<AnnotationMirror, Collection<JediMethod>>() {
                    public Collection<JediMethod> execute(AnnotationMirror value) {
                        return new SithAnnotation(annotationTypeDeclaration, value, environment).getMethodDeclarations(annotationTypeToFactoryMethodWriterMap.get(annotationTypeDeclaration));
                    }
                }));
    }

    private Set<AnnotationMirror> getMirrors(final AnnotationTypeDeclaration annotationTypeDeclaration) {
        return asSet(flatten(environment.getDeclarationsAnnotatedWith(annotationTypeDeclaration), new Functor<Declaration, Collection<AnnotationMirror>>() {
            public Collection<AnnotationMirror> execute(Declaration value) {
                return getMirrors(value, annotationTypeDeclaration);
            }
        }));
    }
}
