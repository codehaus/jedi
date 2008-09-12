package jedi.annotation.writer.method;

import static jedi.functional.Coercions.list;

import java.util.Collection;
import java.util.List;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.jedi.attribute.Attribute;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

public abstract class AbstractProxyFactoryMethodWriter extends AbstractFactoryMethodWriter {
	public AbstractProxyFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
		super(environment);
	}
	
    @Override
    protected final List<Attribute> getExecuteMethodParameters(Annotateable method) {
        return method.getUncutParameters();
    }
    
    @Override
    protected final List<Attribute> getFactoryMethodAdditionalFormalParameters() {
        return list(new Attribute(getDelegateMethodDeclaringType(), RECEIVER_PARAMETER_NAME));
    }

    @Override
    protected final Collection<Attribute> getFactoryMethodBasicParameters() {
        return getMethod().getCutParameters();
    }

    @Override
    protected final String getFactoryMethodNameReturnTypeSuffix() {
        return "Proxy" + super.getFactoryMethodNameReturnTypeSuffix();
    }
}