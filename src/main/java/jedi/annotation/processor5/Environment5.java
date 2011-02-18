package jedi.annotation.processor5;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jedi.annotation.processor.Environment;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.util.SourcePosition;

public class Environment5 implements Environment {
	private final AnnotationProcessorEnvironment underlying;

	public Environment5(AnnotationProcessorEnvironment underlying) {
		this.underlying = underlying;
	}

	@Override
	public void printError(String message) {
		underlying.getMessager().printError(message);
	}

	@Override
	public void printError(final File file, final int line, final int column, String message) {
		underlying.getMessager().printError(new SourcePosition() {
			@Override
			public File file() {
				return file;
			}

			@Override
			public int line() {
				return line;
			}

			@Override
			public int column() {
				return column;
			}
		}, message);
	}

	@Override
	public PrintWriter createSourceFile(String qualifiedClassName) throws IOException {
		return underlying.getFiler().createSourceFile(qualifiedClassName);
	}
}
