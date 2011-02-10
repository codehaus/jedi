package jedi.annotation.writer.method;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.processor.ProcessorOptions;
import jedi.functional.Functor;

public class ProxyFunctorFactoryMethodWriter extends AbstractProxyFactoryMethodWriter {
	public ProxyFunctorFactoryMethodWriter(ProcessorOptions options) {
		super(options);
	}

	@Override
	protected Class<?> getOneParameterClosureClass() {
		return Functor.class;
	}

	@Override
	protected boolean isReturnRequired() {
		return true;
	}

	@Override
	protected boolean hasCorrectReturnType(Annotateable method) {
		return !method.isVoid();
	}

	@Override
	protected void writeClosureTypes() {
		super.writeClosureTypes();
		getWriter().print(", ");
		getWriter().printBoxedQualifiedTypeName(getDelegateMethodReturnType());
	}
}
