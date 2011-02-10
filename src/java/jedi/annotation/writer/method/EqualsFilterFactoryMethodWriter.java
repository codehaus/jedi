package jedi.annotation.writer.method;

import static jedi.functional.Coercions.list;

import java.util.List;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.processor.ProcessorOptions;
import jedi.annotation.writer.method.receiver.EqualsFilterReceiverInvocationWriter;
import jedi.functional.Filter;

public class EqualsFilterFactoryMethodWriter extends AbstractBasicFactoryMethodWriter {
	private static final String TEST_VALUE_PARAMETER_NAME = "$testValue";

	public EqualsFilterFactoryMethodWriter(ProcessorOptions options) {
		super(options);
		setReceiverInvocationWriter(new EqualsFilterReceiverInvocationWriter(getCorrespondingFieldName(TEST_VALUE_PARAMETER_NAME)));
	}

	@Override
	protected String getExecuteMethodReturnType() {
		return Boolean.class.getName();
	}

	@Override
	protected List<Attribute> getFactoryMethodAdditionalFormalParameters() {
		return list(new Attribute(getDelegateMethodReturnType(), TEST_VALUE_PARAMETER_NAME));
	}

	@Override
	protected String getFactoryMethodNameRequiredSuffix() {
		return "Equals";
	}

	@Override
	public Class<?> getOneParameterClosureClass() {
		return Filter.class;
	}

	@Override
	protected boolean hasCorrectReturnType(final Annotateable method) {
		return !(method.isVoid() || method.isBoolean());
	}

	@Override
	protected boolean isReturnRequired() {
		return true;
	}
}
