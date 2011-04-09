package jedi.annotation.processor6;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import jedi.annotation.processor.Environment;

public class Environment6 implements Environment {
	private final ProcessingEnvironment processingEnv;

	public Environment6(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	@Override
	public void printError(String message) {
		processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
	}

	@Override
	public void printError(File file, int line, int column, String message) {
		printError(message + ": " + file.getAbsolutePath() + "(" + line + ", " + column + ")");
	}

	@Override
	public PrintWriter createSourceFile(String qualifiedClassName) throws IOException {
		return new PrintWriter(processingEnv.getFiler().createSourceFile(qualifiedClassName, new Element[0]).openWriter());
	}

}
