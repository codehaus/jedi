package jedi.annotation.writer.method.receiver;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.method.AbstractFactoryMethodWriter;

public class ReceiverInvocationWriter {
    public final void write(JediMethod method, JavaWriter printWriter, boolean returnRequired) {
        printWriter.println("try {");
        writeInvocationWithoutExceptionHandlers(method, printWriter, returnRequired);
        printWriter.println("} catch (RuntimeException ex) {");
        printWriter.println("\tthrow ex;");
        printWriter.println("} catch (Exception ex) {");
        printWriter.println("\tthrow new jedi.JediException(ex);");
        printWriter.println("}");
    }

    private void writeInvocationWithoutExceptionHandlers(JediMethod method, JavaWriter printWriter, boolean returnRequired) {
        printWriter.print('\t');
        if (returnRequired) {
            printWriter.print("return ");
        }
        
        writeProtected(method, printWriter);
        
        printWriter.println(';');
    }

    protected void writeProtected(JediMethod method, JavaWriter writer) {
        writer.print(AbstractFactoryMethodWriter.RECEIVER_PARAMETER_NAME + "." + method.getOriginalName());
        writer.printSimpleNamesAsActualParameterList(method.getParameters());
    }
}
