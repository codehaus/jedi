package jedi.annotation.jedi.attribute;

import jedi.functional.Functor;

public class AttributeBoxedTypeFunctor implements Functor<Attribute, String> {
	public String execute(Attribute value) {
		return value.getBoxedType();
	}
}
