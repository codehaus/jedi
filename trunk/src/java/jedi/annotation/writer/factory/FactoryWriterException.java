package jedi.annotation.writer.factory;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.processor.Environment;

public class FactoryWriterException extends RuntimeException {
	private static final long serialVersionUID = -5943860218162527130L;

	private final Annotateable method;

	public FactoryWriterException(String message, Annotateable method) {
		super(message);
		this.method = method;
	}

	public void write(Environment environment) {
		method.showProcessingError(environment, getMessage());
	}
}
