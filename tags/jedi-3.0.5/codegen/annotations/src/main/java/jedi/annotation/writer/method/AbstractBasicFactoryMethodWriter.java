package jedi.annotation.writer.method;

import static jedi.functional.Coercions.list;
import static jedi.functional.FunctionalPrimitives.append;

import java.util.Collection;
import java.util.List;

import jedi.annotation.processor.ProcessorOptions;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.Attribute;
import jedi.annotation.processor.model.AttributeNameFunctor;
import jedi.functional.Functor;

public abstract class AbstractBasicFactoryMethodWriter extends AbstractFactoryMethodWriter {
	public AbstractBasicFactoryMethodWriter(ProcessorOptions options) {
		super(options);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final List<Attribute> getExecuteMethodParameters(Annotateable method) {
		return append(list(new Attribute(method.getDeclaringTypeWithUnboundedGenerics(), RECEIVER_PARAMETER_NAME)),
				method.getCutParameters());
	}

	@Override
	protected final Collection<Attribute> getFactoryMethodBasicParameters() {
		return getMethod().getUncutParameters();
	}

	@Override
	protected List<Attribute> getFactoryMethodAdditionalFormalParameters() {
		return list();
	}
	
	@Override
	protected Functor<Attribute, String> getExecuteMethodInvocationAttributeNameFunctor() {
		return new AttributeNameFunctor();
	}
}
