package jedi.annotation.writer.factorytype;

import jedi.annotation.processor.model.Attribute;
import jedi.functional.Functor;

public interface ClosureFragmentWriter {
	void writeClosureDeclaration();

	void writeFactoryMethodActualParameters(Functor<Attribute, String> attributeNameFunctor);

	String getFactoryMethodName();

	void writeLocalClass(String className);
}
