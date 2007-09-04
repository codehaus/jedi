package jedi.annotation.sith;

import static jedi.functional.Coercions.*;
import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jedi.annotation.SithCommand;
import jedi.annotation.SithFilter;
import jedi.annotation.SithFunctor;
import jedi.annotation.SithMethods;
import jedi.annotation.jedi.AbstractClosureAnnotationProcessor;
import jedi.annotation.jedi.JediMethod;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;

class ClosureAnnotationProcessor extends AbstractClosureAnnotationProcessor {
    public ClosureAnnotationProcessor(final AnnotationProcessorEnvironment environment) {
        super(environment, SithCommand.class, SithFilter.class, SithFunctor.class);
    }

    private Set<JediMethod> getComposites() {
        final Set<AnnotationMirror> mirrors = getMirrors(getTypeDeclaration(SithMethods.class));
        final Set<JediMethod> methods = getMethods(mirrors, "commands", SithCommand.class);
        methods.addAll(getMethods(mirrors, "functors", SithFunctor.class));
        methods.addAll(getMethods(mirrors, "filters", SithFilter.class));
        return methods;
    }

    @Override
    protected Set<JediMethod> getInterestingDeclarations(final AnnotationTypeDeclaration annotationTypeDeclaration) {
        final Set<JediMethod> methods = getNonComposites(annotationTypeDeclaration);
        methods.addAll(getComposites());
        return methods;
    }

    private Set<JediMethod> getMethods(final Set<AnnotationMirror> mirrors, final String property, final Class< ? > propertyClass) {
        return mirrors == null ? Collections.<JediMethod> emptySet() : asSet(flatten(mirrors, new Functor<AnnotationMirror, Collection<JediMethod>>() {
            @SuppressWarnings("unchecked")
            public Collection<JediMethod> execute(final AnnotationMirror value) {
                return getNonComposites(getTypeDeclaration(propertyClass), getMirrors((List<AnnotationValue>) new AnnotationMirrorInterpreter(value).getValue(property)));
            }
        }));
    }

    private Set<AnnotationMirror> getMirrors(final AnnotationTypeDeclaration annotationTypeDeclaration) {
        return asSet(flatten(environment.getDeclarationsAnnotatedWith(annotationTypeDeclaration), new Functor<Declaration, Collection<AnnotationMirror>>() {
            public Collection<AnnotationMirror> execute(final Declaration value) {
                return getMirrors(value, annotationTypeDeclaration);
            }
        }));
    }

    protected List<AnnotationMirror> getMirrors(final List<AnnotationValue> values) {
        return values == null ? Collections.<AnnotationMirror> emptyList() : collect(values, new Functor<AnnotationValue, AnnotationMirror>() {
            public AnnotationMirror execute(final AnnotationValue value) {
                return (AnnotationMirror) value.getValue();
            }
        });
    }

    private Set<JediMethod> getNonComposites(final AnnotationTypeDeclaration annotationTypeDeclaration) {
        final Set<AnnotationMirror> mirrors = getMirrors(annotationTypeDeclaration);
        return getNonComposites(annotationTypeDeclaration, mirrors);
    }

    private Set<JediMethod> getNonComposites(final AnnotationTypeDeclaration annotationTypeDeclaration, final Collection<AnnotationMirror> mirrors) {
        return asSet(flatten(mirrors, new Functor<AnnotationMirror, Collection<JediMethod>>() {
            public Collection<JediMethod> execute(final AnnotationMirror value) {
                return new SithAnnotation(annotationTypeDeclaration, value, environment).getMethodDeclarations(annotationTypeToFactoryMethodWriterMap.get(annotationTypeDeclaration));
            }
        }));
    }
}
