package jedi.annotation.writer.method;

import static jedi.functional.Coercions.list;

import java.util.Collection;
import java.util.List;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.processor.ProcessorOptions;

public abstract class AbstractProxyFactoryMethodWriter extends AbstractFactoryMethodWriter {
	public AbstractProxyFactoryMethodWriter(ProcessorOptions options) {
		super(options);
	}

	@Override
	protected final List<Attribute> getExecuteMethodParameters(Annotateable method) {
		return method.getUncutParameters();
	}

	@Override
	protected final List<Attribute> getFactoryMethodAdditionalFormalParameters() {
		return list(new Attribute(getDelegateMethodDeclaringType(), RECEIVER_PARAMETER_NAME));
	}

	@Override
	protected final Collection<Attribute> getFactoryMethodBasicParameters() {
		return getMethod().getCutParameters();
	}

	@Override
	protected final String getFactoryMethodNameReturnTypeSuffix() {
		return (options.includeProxySuffix() ? "Proxy" : "") + super.getFactoryMethodNameReturnTypeSuffix();
	}
}