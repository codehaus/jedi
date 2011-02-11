package jedi.annotation.writer.method;

import jedi.annotation.processor.ProcessorOptions;
import jedi.annotation.processor.model.Annotateable;
import jedi.functional.Filter;

public class ProxyFilterFactoryMethodWriter extends AbstractProxyFactoryMethodWriter {
	public ProxyFilterFactoryMethodWriter(ProcessorOptions options) {
		super(options);
	}

	@Override
	protected Class<?> getOneParameterClosureClass() {
		return Filter.class;
	}

	@Override
	protected boolean isReturnRequired() {
		return true;
	}

	@Override
	protected boolean hasCorrectReturnType(Annotateable method) {
		return method.isBoolean();
	}
}
