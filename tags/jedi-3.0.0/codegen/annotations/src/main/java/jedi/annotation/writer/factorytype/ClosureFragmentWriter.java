package jedi.annotation.writer.factorytype;

public interface ClosureFragmentWriter {
	void writeClosureDeclaration();

	void writeFactoryMethodActualParameters();

	String getFactoryMethodName();

	void writeLocalClass(String className);
}
