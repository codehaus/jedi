package jedi.annotation.writer.method;

import java.util.List;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.writer.method.receiver.MembershipFilterReceiverInvocationWriter;
import jedi.functional.Coercions;
import jedi.functional.Filter;

public class MembershipFilterFactoryMethodWriter extends AbstractBasicFactoryMethodWriter {
    private static final String TEST_VALUE_PARAMETER_NAME = "$testValue";

    public MembershipFilterFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
    	super(environment);
        setReceiverInvocationWriter(new MembershipFilterReceiverInvocationWriter(getCorrespondingFieldName(TEST_VALUE_PARAMETER_NAME)));
    }

    @Override
    protected String getExecuteMethodReturnType() {
        return Boolean.class.getName();
    }

    @Override
    protected List<Attribute> getFactoryMethodAdditionalFormalParameters() {
        return Coercions.list(new Attribute("java.util.Collection<? extends " + getBoxedQualifiedTypeName(getDelegateMethodReturnType()) + ">", TEST_VALUE_PARAMETER_NAME));
    }

    @Override
    protected String getFactoryMethodNameRequiredSuffix() {
        return "Membership";
    }

    @Override
    public Class< ? > getOneParameterClosureClass() {
        return Filter.class;
    }

    @Override
    protected boolean hasCorrectReturnType(final JediMethod method) {
        return !(method.isVoidReturnType() || method.isBooleanReturnType());
    }

    @Override
    protected boolean isReturnRequired() {
        return true;
    }
}
