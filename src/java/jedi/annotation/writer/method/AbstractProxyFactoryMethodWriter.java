package jedi.annotation.writer.method;

import java.util.Collection;
import java.util.List;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.jedi.attribute.Attribute;
import jedi.functional.Coercions;

public abstract class AbstractProxyFactoryMethodWriter extends AbstractFactoryMethodWriter {
    @Override
    protected final List<Attribute> getExecuteMethodParameters(JediMethod method) {
        return method.getUncutParameters();
    }
    
    @Override
    protected final List<Attribute> getFactoryMethodAdditionalFormalParameters() {
        return Coercions.list(new Attribute(getDelegateMethodDeclaringType(), RECEIVER_PARAMETER_NAME));
    }

    @Override
    protected final Collection<Attribute> getFactoryMethodBasicParameters() {
        return getMethod().getCutParameters();
    }

    @Override
    protected final String getFactoryMethodNameSuffix() {
        return "Proxy" + super.getFactoryMethodNameSuffix();
    }
}