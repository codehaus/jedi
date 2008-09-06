package jedi.annotation.writer.method;

import java.util.List;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.writer.method.receiver.EqualsFilterReceiverInvocationWriter;
import jedi.functional.Coercions;
import jedi.functional.Filter;

public class EqualsFilterFactoryMethodWriter extends AbstractBasicFactoryMethodWriter {
    private static final String TEST_VALUE_PARAMETER_NAME = "$testValue";

    public EqualsFilterFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
    	super(environment);
        setReceiverInvocationWriter(new EqualsFilterReceiverInvocationWriter(getCorrespondingFieldName(TEST_VALUE_PARAMETER_NAME)));
    }

    @Override
    protected String getExecuteMethodReturnType() {
        return Boolean.class.getName();
    }

    @Override
    protected List<Attribute> getFactoryMethodAdditionalFormalParameters() {
        return Coercions.list(new Attribute(getDelegateMethodReturnType(), TEST_VALUE_PARAMETER_NAME));
    }

    @Override
    protected String getFactoryMethodNameRequiredSuffix() {
        return "Equals";
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
