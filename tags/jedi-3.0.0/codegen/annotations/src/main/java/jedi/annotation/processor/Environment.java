package jedi.annotation.processor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public interface Environment {
	void printError(String message);

	void printError(File file, int line, int column, String message);

	PrintWriter createSourceFile(String qualifiedClassName) throws IOException;
}
