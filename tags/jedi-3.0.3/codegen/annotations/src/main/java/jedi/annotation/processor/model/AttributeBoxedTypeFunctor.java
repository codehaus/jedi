package jedi.annotation.processor.model;

import jedi.functional.Functor;

public class AttributeBoxedTypeFunctor implements Functor<Attribute, String> {
	public String execute(Attribute value) {
		return value.getBoxedType();
	}
}
