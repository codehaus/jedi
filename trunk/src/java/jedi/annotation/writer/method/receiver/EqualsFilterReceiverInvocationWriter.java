package jedi.annotation.writer.method.receiver;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.writer.JavaWriter;

public class EqualsFilterReceiverInvocationWriter extends ReceiverInvocationWriter {
    private final String testValueParameterName;

    public EqualsFilterReceiverInvocationWriter(String testValueParameterName) {
        this.testValueParameterName = testValueParameterName;
    }

    @Override
    protected void writeInvocation(Annotateable method, JavaWriter printWriter) {
        printWriter.print(testValueParameterName);
        printWriter.print(".equals(");
        super.writeInvocation(method, printWriter);
        printWriter.print(")");
    }
}
