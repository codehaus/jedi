package jedi.annotation.writer.method;

import static jedi.functional.Coercions.*;
import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.List;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.jedi.attribute.Attribute;

public abstract class AbstractBasicFactoryMethodWriter extends AbstractFactoryMethodWriter {
    @SuppressWarnings("unchecked")
    @Override
    protected final List<Attribute> getExecuteMethodParameters(JediMethod method) {
        return append(list(new Attribute(method.getDeclaringType(), RECEIVER_PARAMETER_NAME)), method.getCutParameters());
    }
        
    @Override
    protected final Collection<Attribute> getFactoryMethodBasicParameters() {
        return getMethod().getUncutParameters();
    }
    
    @Override
    protected List<Attribute> getFactoryMethodAdditionalFormalParameters() {
        return list();
    }
}
