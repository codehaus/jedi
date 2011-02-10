package jedi.annotation.writer.method;

import static jedi.functional.Coercions.list;
import static jedi.functional.FunctionalPrimitives.append;

import java.util.Collection;
import java.util.List;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.processor.ProcessorOptions;

public abstract class AbstractBasicFactoryMethodWriter extends AbstractFactoryMethodWriter {
	public AbstractBasicFactoryMethodWriter(ProcessorOptions options) {
		super(options);
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
