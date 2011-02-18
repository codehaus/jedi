package jedi.annotation.jedi;

import static jedi.functional.FunctionalPrimitives.group;
import static jedi.functional.FunctionalPrimitives.select;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jedi.annotation.processor.ProcessorOptions;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor5.Environment5;
import jedi.annotation.processor5.OptionAccessor5;
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

public abstract class AbstractClosureAnnotationProcessor implements AnnotationProcessor {
	protected final AnnotationProcessorEnvironment environment;
	private final Map<Class<?>, FactoryMethodWriter> annotationClassToFactoryMethodWriterMap;
	private final FactoryWriter factoryWriter;

	public AbstractClosureAnnotationProcessor(final AnnotationProcessorEnvironment environment, final Class<?> commandAnnotationClass, final Class<?> filterAnnotationClass, final Class<?> functorAnnotationClass, OptionAccessor5 optionAccessor) {
		this.environment = environment;

		annotationClassToFactoryMethodWriterMap = new HashMap<Class<?>, FactoryMethodWriter>();
		final ProcessorOptions options = new ProcessorOptions(optionAccessor);
		annotationClassToFactoryMethodWriterMap.put(commandAnnotationClass, new CompositeFactoryMethodWriter(new CommandFactoryMethodWriter(options), new ProxyCommandFactoryMethodWriter(options)));
		annotationClassToFactoryMethodWriterMap.put(filterAnnotationClass, new CompositeFactoryMethodWriter(new FilterFactoryMethodWriter(options), new EqualsFilterFactoryMethodWriter(options), new MembershipFilterFactoryMethodWriter(options), new ProxyFilterFactoryMethodWriter(options)));
		annotationClassToFactoryMethodWriterMap.put(functorAnnotationClass, new CompositeFactoryMethodWriter(new FunctorFactoryMethodWriter(options), new ProxyFunctorFactoryMethodWriter(options)));

		factoryWriter = new FactoryWriter(new Environment5(environment), annotationClassToFactoryMethodWriterMap);
	}

	private Set<Annotateable> getInterestingDeclarations() {
		final Set<Annotateable> interestingMethodDeclarations = new HashSet<Annotateable>();
		for (final Class<?> annotationClass : annotationClassToFactoryMethodWriterMap.keySet()) {
			interestingMethodDeclarations.addAll(getInterestingNonCompositeDeclarations(annotationClass));
		}
		interestingMethodDeclarations.addAll(getInterestingCompositeDeclarations());
		return interestingMethodDeclarations;
	}

	abstract protected Collection<Annotateable> getInterestingCompositeDeclarations();

	abstract protected Set<Annotateable> getInterestingNonCompositeDeclarations(Class<?> annotationClass);

	private Map<String, List<Annotateable>> getMethodsByQualifiedTypeName() {
		return group(getInterestingDeclarations(), new Functor<Annotateable, String>() {
			public String execute(final Annotateable method) {
				return method.getQualifiedNameOfDeclaringType();
			}
		});
	}

	final protected Collection<AnnotationMirror> getMirrors(final Declaration declaration, final Class<?> annotationClass) {
		return select(declaration.getAnnotationMirrors(), new Filter<AnnotationMirror>() {
			public Boolean execute(final AnnotationMirror mirror) {
				return mirror.getAnnotationType().getDeclaration().equals(getTypeDeclaration(annotationClass));
			}
		});
	}

	protected AnnotationTypeDeclaration getTypeDeclaration(final Class<?> annotationClass) {
		return (AnnotationTypeDeclaration) environment.getTypeDeclaration(annotationClass.getName());
	}

	public void process() {
		process(new InterfaceFactoryType(), new InstanceFactoryType(), new StaticFactoryType());
	}

	private void process(final FactoryType... types) {
		final Map<String, List<Annotateable>> methodsByType = getMethodsByQualifiedTypeName();
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