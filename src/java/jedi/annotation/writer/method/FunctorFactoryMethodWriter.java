package jedi.annotation.writer.method;

import jedi.annotation.jedi.JediMethod;
import jedi.functional.Functor;


public class FunctorFactoryMethodWriter extends AbstractBasicFactoryMethodWriter {    
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
