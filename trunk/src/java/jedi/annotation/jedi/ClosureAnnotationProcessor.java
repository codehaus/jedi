package jedi.annotation.jedi;

import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.flatten;
import static jedi.functional.FunctionalPrimitives.head;
import static jedi.functional.FunctionalPrimitives.select;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jedi.annotation.JediCommand;
import jedi.annotation.JediFilter;
import jedi.annotation.JediFunctor;
import jedi.annotation.sith.AnnotationMirrorInterpreter;
import jedi.annotation.util.AnnotationValueValueFunctor;
import jedi.filters.NotNullFilter;
import jedi.functional.Coercions;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.util.DeclarationFilter;

public class ClosureAnnotationProcessor extends AbstractClosureAnnotationProcessor {
    private static final DeclarationFilter INTERESTING_METHOD_FILTER =
        new DeclarationFilter() {
            @Override
            public boolean matches(Declaration declaration) {
                return declaration instanceof MethodDeclaration;
            }
        }.and(DeclarationFilter.FILTER_PUBLIC.or(DeclarationFilter.FILTER_PACKAGE));

    public ClosureAnnotationProcessor(AnnotationProcessorEnvironment environment) {
        super(environment, JediCommand.class, JediFilter.class, JediFunctor.class);
    }

    @Override
    protected Set<JediMethod> getInterestingDeclarations(final AnnotationTypeDeclaration annotationTypeDeclaration) {
        return Coercions.asSet(flatten(
            ClosureAnnotationProcessor.INTERESTING_METHOD_FILTER.filter(environment.getDeclarationsAnnotatedWith(annotationTypeDeclaration)), new Functor<Declaration, Set<JediMethod>>() {
                public Set<JediMethod> execute(Declaration value) {
                    return getRequiredMethods(annotationTypeDeclaration, (MethodDeclaration) value);
                }
            }));
    }

    @SuppressWarnings("unchecked")
    private Set<JediMethod> getRequiredMethods(final AnnotationTypeDeclaration annotationTypeDeclaration, MethodDeclaration method) {
        AnnotationMirrorInterpreter interpreter = new AnnotationMirrorInterpreter(head(getMirrors(method, annotationTypeDeclaration)));
        String factoryPrefix = (String) interpreter.getValue("name");
        if (factoryPrefix == null) {
            factoryPrefix = method.getSimpleName();
        }

        List<AnnotationValue> value = (List<AnnotationValue>) interpreter.getValue("cut");
        return value != null
            ? getCuts(annotationTypeDeclaration, method, value, factoryPrefix)
            : Coercions.set(new JediMethod(method, annotationTypeToFactoryMethodWriterMap.get(annotationTypeDeclaration), factoryPrefix));
    }

    private Set<JediMethod> getCuts(final AnnotationTypeDeclaration annotationTypeDeclaration, final MethodDeclaration method, List<AnnotationValue> cuts, final String factoryPrefix) {
        return Coercions.asSet(
                select(collect(cuts, new Functor<AnnotationValue, JediMethod>() {
				    public JediMethod execute(AnnotationValue value) {
				        return createCutMethod(annotationTypeDeclaration, method, ((AnnotationMirror) value.getValue()), factoryPrefix);
				    }
				}),
                    new NotNullFilter<JediMethod>()));
    }

    @SuppressWarnings("unchecked")
    private JediMethod createCutMethod(AnnotationTypeDeclaration annotationTypeDeclaration, MethodDeclaration method, AnnotationMirror cut, String factoryPrefix) {
        AnnotationMirrorInterpreter interpreter = new AnnotationMirrorInterpreter(cut);

        String name = (String) interpreter.getValue("name");
        if (name == null) {
            name = factoryPrefix;
        }
        List<String> parameterNames = getCutParameterNames((List<AnnotationValue>) interpreter.getValue("parameters"));

        return validateCutParameters(method, parameterNames)
            ? new JediMethod(method, annotationTypeToFactoryMethodWriterMap.get(annotationTypeDeclaration), name, Coercions.asSet(parameterNames))
            : null;

    }

    private boolean validateCutParameters(MethodDeclaration method, List<String> parameterNames) {
        List<String> outstanding = Coercions.asList(parameterNames);
        outstanding.removeAll(getNames(method.getParameters()));
        if (outstanding.isEmpty()) {
            return true;
        }

        environment.getMessager().printError(method.getPosition(), "Cut parameters do not exist in formal parameter list: " + outstanding);
        return false;
    }

    private List<String> getNames(Collection<ParameterDeclaration> parameters) {
        return collect(parameters, new Functor<ParameterDeclaration, String>() {
            public String execute(ParameterDeclaration value) {
                return value.getSimpleName();
            }
        });
    }

    private List<String> getCutParameterNames(List<AnnotationValue> values) {
        return values == null ? Collections.<String>emptyList() : collect(values, new AnnotationValueValueFunctor<String>());
    }
}