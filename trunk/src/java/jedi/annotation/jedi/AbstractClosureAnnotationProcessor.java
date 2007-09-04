package jedi.annotation.jedi;

import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import jedi.annotation.writer.factory.FactoryWriter;
import jedi.annotation.writer.factorytype.FactoryType;
import jedi.annotation.writer.factorytype.InstanceFactoryType;
import jedi.annotation.writer.factorytype.InterfaceFactoryType;
import jedi.annotation.writer.factorytype.StaticFactoryType;
import jedi.annotation.writer.method.CommandFactoryMethodWriter;
import jedi.annotation.writer.method.CompositeFactoryMethodWriter;
import jedi.annotation.writer.method.EqualsFilterFactoryMethodWriter;
import jedi.annotation.writer.method.FactoryMethodWriter;
import jedi.annotation.writer.method.FilterFactoryMethodWriter;
import jedi.annotation.writer.method.FunctorFactoryMethodWriter;
import jedi.annotation.writer.method.MembershipFilterFactoryMethodWriter;
import jedi.annotation.writer.method.ProxyCommandFactoryMethodWriter;
import jedi.annotation.writer.method.ProxyFilterFactoryMethodWriter;
import jedi.annotation.writer.method.ProxyFunctorFactoryMethodWriter;
import jedi.functional.Filter;
import jedi.functional.Functor;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.TypeDeclaration;

public abstract class AbstractClosureAnnotationProcessor implements AnnotationProcessor {
    protected final AnnotationProcessorEnvironment environment;
    protected final Map<AnnotationTypeDeclaration, FactoryMethodWriter> annotationTypeToFactoryMethodWriterMap;

    public AbstractClosureAnnotationProcessor(final AnnotationProcessorEnvironment environment, final Class< ? > commandAnnotationClass, final Class< ? > filterAnnotationClass, final Class< ? > functorAnnotationClass) {
        this.environment = environment;

        annotationTypeToFactoryMethodWriterMap = new HashMap<AnnotationTypeDeclaration, FactoryMethodWriter>();
        putAnnotationClassToWriterMapping(commandAnnotationClass, new CompositeFactoryMethodWriter(new CommandFactoryMethodWriter(), new ProxyCommandFactoryMethodWriter()));
        putAnnotationClassToWriterMapping(filterAnnotationClass, new CompositeFactoryMethodWriter(new FilterFactoryMethodWriter(), new EqualsFilterFactoryMethodWriter(), new MembershipFilterFactoryMethodWriter(), new ProxyFilterFactoryMethodWriter()));
        putAnnotationClassToWriterMapping(functorAnnotationClass, new CompositeFactoryMethodWriter(new FunctorFactoryMethodWriter(), new ProxyFunctorFactoryMethodWriter()));
    }

    private Set<JediMethod> getInterestingDeclarations() {
        final Set<JediMethod> interestingMethodDeclarations = new HashSet<JediMethod>();
        for (final AnnotationTypeDeclaration annotationTypeDeclaration : annotationTypeToFactoryMethodWriterMap.keySet()) {
            interestingMethodDeclarations.addAll(getInterestingDeclarations(annotationTypeDeclaration));
        }
        return interestingMethodDeclarations;
    }

    abstract protected Collection< ? extends JediMethod> getInterestingDeclarations(AnnotationTypeDeclaration annotationTypeDeclaration);

    private Map<TypeDeclaration, List<JediMethod>> getMethodsByType() {
        return group(getInterestingDeclarations(), new Functor<JediMethod, TypeDeclaration>() {
            public TypeDeclaration execute(final JediMethod method) {
                return method.getDeclaringType();
            }
        });
    }

    protected Collection<AnnotationMirror> getMirrors(final Declaration declaration, final AnnotationTypeDeclaration annotationTypeDeclaration) {
        return select(declaration.getAnnotationMirrors(), new Filter<AnnotationMirror>() {
            public Boolean execute(final AnnotationMirror mirror) {
                return mirror.getAnnotationType().getDeclaration().equals(annotationTypeDeclaration);
            }
        });
    }

    protected AnnotationTypeDeclaration getTypeDeclaration(final Class< ? > annotationClass) {
        return (AnnotationTypeDeclaration) environment.getTypeDeclaration(annotationClass.getName());
    }

    public void process() {
        process(new InterfaceFactoryType(), new InstanceFactoryType(), new StaticFactoryType());
    }

    protected void process(final FactoryType... types) {
        final Map<TypeDeclaration, List<JediMethod>> methodsByType = getMethodsByType();
        for (final FactoryType type : types) {
            writeFactories(methodsByType, type);
        }
    }

    private void putAnnotationClassToWriterMapping(final Class< ? > commandAnnotationClass, final FactoryMethodWriter writer) {
        annotationTypeToFactoryMethodWriterMap.put(getTypeDeclaration(commandAnnotationClass), writer);
    }

    private void writeFactories(final Map<TypeDeclaration, List<JediMethod>> methodsByType, final FactoryType type) {
        for (final Entry<TypeDeclaration, List<JediMethod>> entry : methodsByType.entrySet()) {
            new FactoryWriter(environment, entry.getKey(), type, annotationTypeToFactoryMethodWriterMap).write(entry.getValue());
        }
    }
}