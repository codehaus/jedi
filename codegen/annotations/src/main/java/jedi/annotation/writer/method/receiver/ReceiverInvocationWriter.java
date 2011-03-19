package jedi.annotation.writer.method.receiver;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.method.AbstractFactoryMethodWriter;

public class ReceiverInvocationWriter {
	public final void write(Annotateable method, JavaWriter printWriter, boolean returnRequired) {
		printWriter.print("try").openBlock();
		writeInvocationWithoutExceptionHandlers(method, printWriter, returnRequired);
		printWriter.closeBlock().print("catch (RuntimeException ex)").openBlock();
		printWriter.println("throw ex;");
		printWriter.closeBlock().print("catch (Exception ex)").openBlock();
		printWriter.println("throw new jedi.JediException(ex);");
		printWriter.closeBlock();
	}

	private void writeInvocationWithoutExceptionHandlers(Annotateable method, JavaWriter writer, boolean returnRequired) {
		if (returnRequired) {
			writer.print("return ");
		}

		writeInvocation(method, writer);

		writer.println(';');
	}

	protected void writeInvocation(Annotateable method, JavaWriter printWriter) {
		method.writeInvocation(printWriter, AbstractFactoryMethodWriter.RECEIVER_PARAMETER_NAME);
	}
}
