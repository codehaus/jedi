package jedi.annotation.writer.method.receiver;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.Attribute;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.method.AbstractFactoryMethodWriter;
import jedi.functional.Functor;

public class ReceiverInvocationWriter {
	public final void write(Annotateable method, JavaWriter printWriter, boolean returnRequired, Functor<Attribute, String> attributeNameFunctor) {
		printWriter.print("try").openBlock();
		writeInvocationWithoutExceptionHandlers(method, printWriter, returnRequired, attributeNameFunctor);
		printWriter.closeBlock().print("catch (RuntimeException ex)").openBlock();
		printWriter.println("throw ex;");
		printWriter.closeBlock().print("catch (Exception ex)").openBlock();
		printWriter.println("throw new jedi.JediException(ex);");
		printWriter.closeBlock();
	}

	private void writeInvocationWithoutExceptionHandlers(Annotateable method, JavaWriter writer, boolean returnRequired, Functor<Attribute, String> attributeNameFunctor) {
		if (returnRequired) {
			writer.print("return ");
		}

		writeInvocation(method, writer, attributeNameFunctor);

		writer.println(';');
	}

	protected void writeInvocation(Annotateable method, JavaWriter printWriter, Functor<Attribute, String> attributeNameFunctor) {
		method.writeInvocation(printWriter, AbstractFactoryMethodWriter.RECEIVER_PARAMETER_NAME, attributeNameFunctor);
	}
}
