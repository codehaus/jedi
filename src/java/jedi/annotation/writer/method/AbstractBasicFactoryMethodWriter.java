package jedi.annotation.writer.method;

import static jedi.functional.Coercions.*;
import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.List;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.jedi.attribute.Attribute;

public abstract class AbstractBasicFactoryMethodWriter extends AbstractFactoryMethodWriter {
	public AbstractBasicFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
		super(environment);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final List<Attribute> getExecuteMethodParameters(Annotateable method) {
		return append(list(new Attribute(method.getDeclaringType(), RECEIVER_PARAMETER_NAME)), method.getCutParameters());
	}

	@Override
	protected final Collection<Attribute> getFactoryMethodBasicParameters() {
		return getMethod().getUncutParameters();
	}

	@Override
	protected List<Attribute> getFactoryMethodAdditionalFormalParameters() {
		return list();
	}
}
