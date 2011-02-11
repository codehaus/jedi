package jedi.annotation.writer.method.receiver;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.method.AbstractFactoryMethodWriter;

public class ReceiverInvocationWriter {
	public final void write(Annotateable method, JavaWriter printWriter, boolean returnRequired) {
		printWriter.println("try {");
		writeInvocationWithoutExceptionHandlers(method, printWriter, returnRequired);
		printWriter.println("} catch (RuntimeException ex) {");
		printWriter.println("\tthrow ex;");
		printWriter.println("} catch (Exception ex) {");
		printWriter.println("\tthrow new jedi.JediException(ex);");
		printWriter.println("}");
	}

	private void writeInvocationWithoutExceptionHandlers(Annotateable method, JavaWriter printWriter, boolean returnRequired) {
		printWriter.print('\t');
		if (returnRequired) {
			printWriter.print("return ");
		}

		writeInvocation(method, printWriter);

		printWriter.println(';');
	}

	protected void writeInvocation(Annotateable method, JavaWriter printWriter) {
		method.writeInvocation(printWriter, AbstractFactoryMethodWriter.RECEIVER_PARAMETER_NAME);
	}
}
