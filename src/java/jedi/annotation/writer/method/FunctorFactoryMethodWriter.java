package jedi.annotation.writer.method;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

import jedi.annotation.jedi.JediMethod;
import jedi.functional.Functor;


public class FunctorFactoryMethodWriter extends AbstractBasicFactoryMethodWriter {
	public FunctorFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
		super(environment);
	}
	
    @Override
    public Class< ? > getOneParameterClosureClass() {
        return Functor.class;
    }
    
    @Override
    protected boolean isReturnRequired() {
        return true;
    }

    @Override
    protected void writeClosureTypes() {
        super.writeClosureTypes();
        getWriter().print(", ");
        getWriter().printBoxedQualifiedTypeName(getDelegateMethodReturnType());
    }

    @Override
    protected boolean hasCorrectReturnType(JediMethod method) {
        return !method.isVoidReturnType();
    }
}
