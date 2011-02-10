package jedi.annotation.writer.method;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.processor.ProcessorOptions;
import jedi.functional.Command;

public class ProxyCommandFactoryMethodWriter extends AbstractProxyFactoryMethodWriter {
	public ProxyCommandFactoryMethodWriter(ProcessorOptions options) {
		super(options);
	}

	@Override
	protected Class<?> getOneParameterClosureClass() {
		return Command.class;
	}

	@Override
	protected boolean hasCorrectReturnType(Annotateable method) {
		return true;
	}
}
