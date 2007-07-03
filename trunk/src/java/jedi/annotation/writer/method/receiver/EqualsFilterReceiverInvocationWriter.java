package jedi.annotation.writer.method.receiver;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.writer.JavaWriter;

public class EqualsFilterReceiverInvocationWriter extends ReceiverInvocationWriter {
    private final String testValueParameterName;

    public EqualsFilterReceiverInvocationWriter(String testValueParameterName) {
        this.testValueParameterName = testValueParameterName;
    }

    @Override
    protected void writeProtected(JediMethod method, JavaWriter printWriter) {
        printWriter.print(testValueParameterName);
        printWriter.print(".equals(");
        super.writeProtected(method, printWriter);
        printWriter.print(")");
    }
}
