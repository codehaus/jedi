package jedi.annotation.jedi.attribute;

import jedi.functional.Functor;

public class AttributeNameFunctor implements Functor<Attribute, String> {
    public String execute(Attribute v) {
        return v.getName();
    }
}
