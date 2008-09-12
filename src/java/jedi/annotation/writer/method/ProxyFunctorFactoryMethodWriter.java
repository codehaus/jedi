package jedi.annotation.writer.method;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

import jedi.annotation.jedi.Annotateable;
import jedi.functional.Functor;

public class ProxyFunctorFactoryMethodWriter extends AbstractProxyFactoryMethodWriter {
	public ProxyFunctorFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
		super(environment);
	}
	
    @Override
    protected Class< ? > getOneParameterClosureClass() {
        return Functor.class;
    }
    
    @Override
    protected boolean isReturnRequired() {
        return true;
    }

    @Override
    protected boolean hasCorrectReturnType(Annotateable method) {
        return !method.isVoid();
    }

    @Override
    protected void writeClosureTypes() {
        super.writeClosureTypes();
        getWriter().print(", ");
        getWriter().printBoxedQualifiedTypeName(getDelegateMethodReturnType());
    }
}
