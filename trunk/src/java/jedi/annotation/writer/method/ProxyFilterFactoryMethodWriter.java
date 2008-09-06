package jedi.annotation.writer.method;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

import jedi.annotation.jedi.JediMethod;
import jedi.functional.Filter;

public class ProxyFilterFactoryMethodWriter extends AbstractProxyFactoryMethodWriter {
	public ProxyFilterFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
		super(environment);
	}
	
    @Override
    protected Class< ? > getOneParameterClosureClass() {
        return Filter.class;
    }
    
    @Override
    protected boolean isReturnRequired() {
        return true;
    }

    @Override
    protected boolean hasCorrectReturnType(JediMethod method) {
        return method.isBooleanReturnType();
    }
}
