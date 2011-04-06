package jedi.annotation.writer.method;

import static jedi.functional.Coercions.list;

import java.util.List;

import jedi.annotation.processor.ProcessorOptions;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.Attribute;
import jedi.annotation.writer.method.receiver.MembershipFilterReceiverInvocationWriter;
import jedi.functional.Filter;

public class MembershipFilterFactoryMethodWriter extends AbstractBasicFactoryMethodWriter {
	private static final String TEST_VALUE_PARAMETER_NAME = "$testValue";

	public MembershipFilterFactoryMethodWriter(ProcessorOptions options) {
		super(options);
		setReceiverInvocationWriter(new MembershipFilterReceiverInvocationWriter(TEST_VALUE_PARAMETER_NAME));
	}

	@Override
	protected String getExecuteMethodReturnType() {
		return Boolean.class.getName();
	}

	@Override
	protected List<Attribute> getFactoryMethodAdditionalFormalParameters() {
		return list(new Attribute("java.util.Collection<? extends " + getDelegateMethodReturnType() + ">", TEST_VALUE_PARAMETER_NAME));
	}

	@Override
	protected String getFactoryMethodNameRequiredSuffix() {
		return "Membership";
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
