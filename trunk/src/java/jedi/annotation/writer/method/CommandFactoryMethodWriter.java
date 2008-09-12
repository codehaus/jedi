package jedi.annotation.writer.method;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

import jedi.annotation.jedi.Annotateable;
import jedi.functional.Command;

public class CommandFactoryMethodWriter extends AbstractBasicFactoryMethodWriter {
	public CommandFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
		super(environment);
	}
	
    @Override
    protected Class< ? > getOneParameterClosureClass() {
        return Command.class;
    }

    @Override
    protected boolean hasCorrectReturnType(Annotateable method) {
        return true;
    }
}
