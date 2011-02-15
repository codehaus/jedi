package jedi.annotation.writer.method.receiver;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.writer.JavaWriter;

public class MembershipFilterReceiverInvocationWriter extends ReceiverInvocationWriter {
	private final String testValueParameterName;

	public MembershipFilterReceiverInvocationWriter(String testValueParameterName) {
		this.testValueParameterName = testValueParameterName;
	}

	@Override
	protected void writeInvocation(Annotateable method, JavaWriter printWriter) {
		printWriter.print(testValueParameterName);
		printWriter.print(".contains(");
		super.writeInvocation(method, printWriter);
		printWriter.print(")");
	}
}
