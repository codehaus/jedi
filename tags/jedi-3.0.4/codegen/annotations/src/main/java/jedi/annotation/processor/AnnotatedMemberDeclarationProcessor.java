package jedi.annotation.processor;

import static jedi.functional.FunctionalPrimitives.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jedi.annotation.processor.model.Annotateable;
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
import jedi.functional.Functor;


public class AnnotatedMemberDeclarationProcessor {
	private final Map<Class<?>, FactoryMethodWriter> annotationClassToFactoryMethodWriterMap;
	private final FactoryWriter factoryWriter;

	public AnnotatedMemberDeclarationProcessor(final Class<?> commandAnnotationClass, final Class<?> filterAnnotationClass, final Class<?> functorAnnotationClass, ProcessorOptionAccessor optionAccessor, Environment environment) {
		annotationClassToFactoryMethodWriterMap = new HashMap<Class<?>, FactoryMethodWriter>();
		final ProcessorOptions options = new ProcessorOptions(optionAccessor);
		annotationClassToFactoryMethodWriterMap.put(commandAnnotationClass, new CompositeFactoryMethodWriter(new CommandFactoryMethodWriter(options), new ProxyCommandFactoryMethodWriter(options)));
		annotationClassToFactoryMethodWriterMap.put(filterAnnotationClass, new CompositeFactoryMethodWriter(new FilterFactoryMethodWriter(options), new EqualsFilterFactoryMethodWriter(options), new MembershipFilterFactoryMethodWriter(options), new ProxyFilterFactoryMethodWriter(options)));
		annotationClassToFactoryMethodWriterMap.put(functorAnnotationClass, new CompositeFactoryMethodWriter(new FunctorFactoryMethodWriter(options), new ProxyFunctorFactoryMethodWriter(options)));

		factoryWriter = new FactoryWriter(environment, annotationClassToFactoryMethodWriterMap);
	}

	private Map<String, List<Annotateable>> getMethodsByQualifiedTypeName(Set<Annotateable> annotatedMemberDeclarations) {
		return group(annotatedMemberDeclarations, new Functor<Annotateable, String>() {
			public String execute(final Annotateable method) {
				return method.getQualifiedNameOfDeclaringType();
			}
		});
	}

	public void process(Set<Annotateable> annotatedMemberDeclarations) {
		process(annotatedMemberDeclarations, new InterfaceFactoryType(), new InstanceFactoryType(), new StaticFactoryType());
	}

	private void process(Set<Annotateable> annotatedMemberDeclarations, final FactoryType... types) {
		final Map<String, List<Annotateable>> methodsByType = getMethodsByQualifiedTypeName(annotatedMemberDeclarations);
		for (final FactoryType type : types) {
			writeFactories(methodsByType, type);
		}
	}

	private void writeFactories(final Map<String, List<Annotateable>> methodsByType, final FactoryType factoryType) {
		for (final List<Annotateable> annotateables : methodsByType.values()) {
			factoryWriter.write(annotateables, factoryType);
		}
	}
}