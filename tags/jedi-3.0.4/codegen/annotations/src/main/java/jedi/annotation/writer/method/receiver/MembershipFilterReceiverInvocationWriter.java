package jedi.annotation.writer.method.receiver;

import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.Attribute;
import jedi.annotation.writer.JavaWriter;
import jedi.functional.Functor;

public class MembershipFilterReceiverInvocationWriter extends ReceiverInvocationWriter {
	private final String testValueParameterName;

	public MembershipFilterReceiverInvocationWriter(String testValueParameterName) {
		this.testValueParameterName = testValueParameterName;
	}

	@Override
	protected void writeInvocation(Annotateable method, JavaWriter printWriter, Functor<Attribute, String> attributeNameFunctor) {
		printWriter.print(testValueParameterName);
		printWriter.print(".contains(");
		super.writeInvocation(method, printWriter, attributeNameFunctor);
		printWriter.print(")");
	}
}
