package jedi.annotation.sith;

import java.util.Map;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;

public class AnnotationMirrorInterpreter {
    private final AnnotationMirror annotationMirror;

    public AnnotationMirrorInterpreter(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
    }
    
    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public Object getValue(String key) {
        final Map<AnnotationTypeElementDeclaration, AnnotationValue> values = annotationMirror.getElementValues();
        for (AnnotationTypeElementDeclaration ted : values.keySet()) {
            if (ted.getSimpleName().equals(key)) {
                return values.get(ted).getValue();
            }
        }
        return null;
    }
}
