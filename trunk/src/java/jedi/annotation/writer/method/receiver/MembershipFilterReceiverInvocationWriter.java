package jedi.annotation.writer.method.receiver;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.writer.JavaWriter;

public class MembershipFilterReceiverInvocationWriter extends ReceiverInvocationWriter {
    private final String testValueParameterName;

    public MembershipFilterReceiverInvocationWriter(String testValueParameterName) {
        this.testValueParameterName = testValueParameterName;
    }
    
    @Override
    protected void writeProtected(JediMethod method, JavaWriter printWriter) {
        printWriter.print(testValueParameterName);
        printWriter.print(".contains(");
        super.writeProtected(method, printWriter);
        printWriter.print(")");
    }
}
