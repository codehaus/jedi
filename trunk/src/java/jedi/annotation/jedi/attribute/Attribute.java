package jedi.annotation.jedi.attribute;

import jedi.annotation.util.BoxerFunctor;

import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.type.TypeMirror;

public class Attribute {
	private final String type;
	private final String boxedType;
	private final String name;

	public Attribute(ParameterDeclaration declaration) {
		this(declaration.getType(), declaration.getSimpleName());
	}

	public Attribute(TypeMirror mirror, String name) {
		this(mirror.toString(), new BoxerFunctor().execute(mirror), name);
	}

	private Attribute(String type, String boxedType, String name) {
		this.type = type;
		this.boxedType = boxedType;
		this.name = name;
	}

	public Attribute(String boxedType, String name) {
		this(boxedType, boxedType, name);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getBoxedType() {
		return boxedType;
	}
}
