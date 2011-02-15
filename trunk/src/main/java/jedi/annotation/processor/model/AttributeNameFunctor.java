package jedi.annotation.processor.model;

import jedi.functional.Functor;

public class AttributeNameFunctor implements Functor<Attribute, String> {
	public String execute(Attribute v) {
		return v.getName();
	}
}
