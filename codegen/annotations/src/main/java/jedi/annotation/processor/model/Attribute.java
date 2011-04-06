package jedi.annotation.processor.model;

public class Attribute {
	private final String type;
	private final String boxedType;
	private final String name;

	public Attribute(String type, String boxedType, String name) {
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

	public String getHashCodeExpression() {
		return isPrimitive() ? boxedType + ".valueOf(" + name + ").hashCode()" : name + " == null ? 0 : " + name + ".hashCode()";
	}

	public String getEqualsMethodExpression(final String prefix) {
		return isPrimitive() ? (prefix + name + " == " + name) : (prefix + name + " == null ? " + name + " == null : " + prefix + name + ".equals(" + name + ")");
	}

	public boolean isPrimitive() {
		return !getBoxedType().equals(getType());
	}
}
