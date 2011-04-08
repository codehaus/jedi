package jedi.annotation.processor5.model;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;

import java.util.Map;

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
		for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> ted : values.entrySet()) {
			if (ted.getKey().getSimpleName().equals(key)) {
				return ted.getValue();
			}
		}
		return null;
	}
}
