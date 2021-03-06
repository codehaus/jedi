package jedi.annotation.writer.method;

import jedi.annotation.processor.ProcessorOptions;
import jedi.annotation.processor.model.Annotateable;
import jedi.functional.Functor;

public class FunctorFactoryMethodWriter extends AbstractBasicFactoryMethodWriter {
	public FunctorFactoryMethodWriter(ProcessorOptions options) {
		super(options);
	}

	@Override
	public Class<?> getOneParameterClosureClass() {
		return Functor.class;
	}

	@Override
	protected boolean isReturnRequired() {
		return true;
	}

	@Override
	protected void writeClosureTypes() {
		super.writeClosureTypes();
		getWriter()
			.print(", ")
			.print(getDelegateMethodReturnType());
	}

	@Override
	protected boolean hasCorrectReturnType(Annotateable method) {
		return !method.isVoid();
	}
}
