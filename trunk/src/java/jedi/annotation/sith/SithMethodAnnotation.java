package jedi.annotation.sith;

import static jedi.functional.FunctionalPrimitives.*;

import java.util.List;

import jedi.annotation.util.AnnotationValueValueFunctor;
import jedi.functional.Functor;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.type.TypeMirror;

class SithMethodAnnotation extends AnnotationMirrorInterpreter {
    private final SithAnnotation type;

    public SithMethodAnnotation(SithAnnotation sithAnnotation, AnnotationMirror annotationMirror) {
        super(annotationMirror);
        this.type = sithAnnotation;
    }
    
    private String getName() {
        return (String) getValue("name");
    }
    
    @SuppressWarnings("unchecked")
    private List<TypeMirror> getParameterTypes() {
        return collect((List<AnnotationValue>) getValue("parameterTypes"), new AnnotationValueValueFunctor<TypeMirror>());
    }
    
    public MethodDeclaration getMethodDeclaration(Messager messager) {
        for (MethodDeclaration methodDeclaration : type.getType().getMethods()) {
            if (isMatch(methodDeclaration)) {
                return methodDeclaration;
            }
        }
        
        messager.printError(type.getPosition(), "No such method. name : " + getName() + ", parameters : " + getParameterTypes());
        return null;
    }
    
    
    private boolean isMatch(MethodDeclaration declaration) {
        if (!declaration.getSimpleName().equals(getName())) {
            return false;
        }
        
        final List<TypeMirror> requiredParameterTypes = getParameterTypes();
        final List<TypeMirror> formalTypeParameters = collect(
                declaration.getParameters(), new Functor<ParameterDeclaration, TypeMirror>() {
                    public TypeMirror execute(ParameterDeclaration value) {
                        return value.getType();
                    }
                });
        
        return requiredParameterTypes.equals(formalTypeParameters);
    }

    @Override
    public String toString() {
        return getName() + " : " + getParameterTypes();
    }
}
