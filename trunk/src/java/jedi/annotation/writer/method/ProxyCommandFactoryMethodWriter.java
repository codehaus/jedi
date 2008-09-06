package jedi.annotation.writer.method;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

import jedi.annotation.jedi.JediMethod;
import jedi.functional.Command;

public class ProxyCommandFactoryMethodWriter extends AbstractProxyFactoryMethodWriter {
	public ProxyCommandFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
		super(environment);
	}
	
    @Override
    protected Class< ? > getOneParameterClosureClass() {
        return Command.class;
    }
    
    @Override
    protected boolean hasCorrectReturnType(JediMethod method) {
        return true;
    }
}
