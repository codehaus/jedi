package jedi.annotation.writer.method;

import java.util.List;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.writer.method.receiver.MembershipFilterReceiverInvocationWriter;
import jedi.functional.Coercions;
import jedi.functional.Filter;

public class MembershipFilterFactoryMethodWriter extends AbstractBasicFactoryMethodWriter {
    private static final String TEST_VALUE_PARAMETER_NAME = "$testValue";

    public MembershipFilterFactoryMethodWriter() {
        setReceiverInvocationWriter(new MembershipFilterReceiverInvocationWriter(TEST_VALUE_PARAMETER_NAME));
    }
    
    @Override
    public Class< ? > getOneParameterClosureClass() {
        return Filter.class;
    }
    
    @Override
    protected boolean isReturnRequired() {
        return true;
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
    protected String getFactoryMethodNameSuffix() {
        return "Membership" + super.getFactoryMethodNameSuffix();
    }

    @Override
    protected boolean hasCorrectReturnType(JediMethod method) {
        return !(method.isVoidReturnType() || method.isBooleanReturnType());
    }
}
