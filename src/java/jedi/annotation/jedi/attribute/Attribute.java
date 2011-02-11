package jedi.annotation.jedi.attribute;


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
}
