package jedi.annotation.writer.method;

import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.fold;
import static jedi.functional.FunctionalPrimitives.join;

import java.util.Collection;
import java.util.List;

import jedi.annotation.processor.ProcessorOptions;
import jedi.annotation.processor.model.Annotateable;
import jedi.annotation.processor.model.Attribute;
import jedi.annotation.processor.model.AttributeNameFunctor;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.factory.FactoryWriterException;
import jedi.annotation.writer.factorytype.ClosureFragmentWriter;
import jedi.annotation.writer.factorytype.FactoryType;
import jedi.annotation.writer.method.receiver.ReceiverInvocationWriter;
import jedi.functional.Functor;
import jedi.functional.Functor2;

public abstract class AbstractFactoryMethodWriter implements ClosureFragmentWriter, FactoryMethodWriter {
	public static final String RECEIVER_PARAMETER_NAME = "$receiver";

	private JavaWriter writer;
	private FactoryType factoryType;
	private Annotateable method;
	protected final ProcessorOptions options;

	private ReceiverInvocationWriter receiverInvocationWriter = new ReceiverInvocationWriter();

	public AbstractFactoryMethodWriter(ProcessorOptions options) {
		this.options = options;
	}

	public final boolean canHandle(final Annotateable method) {
		return hasCorrectNumberOfParameters(getExecuteMethodParameters(method).size()) && hasCorrectReturnType(method);
	}

	protected final void checkPrecondition(boolean precondition, final String message) {
		if (!precondition) {
			throw new FactoryWriterException(message, method);
		}
	}

	private Functor<Attribute, String> createFactoryMethodParameterEqualityFunctor(final String prefix) {
		return new Functor<Attribute, String>() {
			public String execute(final Attribute attribute) {
				return "(" + attribute.getEqualsMethodExpression(prefix) + ")";
			}
		};
	}
	
	private Functor<Attribute, Attribute> createAttributeNamePrefixingFunctor(final String prefix) {
		return new Functor<Attribute, Attribute>() {
			public Attribute execute(Attribute value) {
				return new Attribute(value.getType(), prefix + value.getName());
			}
		};
	}

	private Functor2<String, Attribute, String> createHashCodeFoldFunctor() {
		return new Functor2<String, Attribute, String>() {
			public String execute(final String accumulator, final Attribute attribute) {
				return "(" + accumulator + ") * 37 + (" + attribute.getHashCodeExpression() + ")";
			}
		};
	}
	
	public final void execute(final Annotateable method) {
		this.method = method;

		writeJavadoc();
		startMethod();
		factoryType.writeMethodBody(this, writer);

		this.method = null;
	}

	protected final String getDelegateMethodDeclaringTypeWithoutBounds() {
		return method.getDeclaringTypeWithUnboundedGenerics();
	}

	protected final String getDelegateMethodReturnType() {
		return method.getBoxedDeclaredType();
	}

	protected final List<Attribute> getExecuteMethodParameters() {
		return getExecuteMethodParameters(getMethod());
	}

	protected abstract List<Attribute> getExecuteMethodParameters(Annotateable method);

	protected String getExecuteMethodReturnType() {
		return getDelegateMethodReturnType();
	}

	protected abstract Collection<Attribute> getFactoryMethodAdditionalFormalParameters();

	protected abstract Collection<Attribute> getFactoryMethodBasicParameters();

	public final String getFactoryMethodName() {
		return getStartOfMethodName() + getFactoryMethodNameRequiredSuffix() + (options.includeSuffixes() ? getFactoryMethodNameReturnTypeSuffix() : "");
	}

	private String getStartOfMethodName() {
		return method.getName(!options.includeAccessorVerbs());
	}

	protected String getFactoryMethodNameRequiredSuffix() {
		return "";
	}

	protected String getFactoryMethodNameReturnTypeSuffix() {
		return options.includeClosureTypeSuffix() ? getReturnType().getSimpleName() : "";
	}

	@SuppressWarnings("unchecked")
	protected List<Attribute> getFactoryMethodParameters() {
		return append(getFactoryMethodAdditionalFormalParameters(), getFactoryMethodBasicParameters());
	}

	protected Annotateable getMethod() {
		return method;
	}

	protected abstract Class<?> getOneParameterClosureClass();

	private Class<?> getReturnType() {
		final int n = getExecuteMethodParameters().size();
		try {
			return n == 1 ? getOneParameterClosureClass() : Class.forName(getOneParameterClosureClass().getName() + n);
		} catch (final ClassNotFoundException e) {
			throw new FactoryWriterException("No appropriate closure for method with " + n + " pararmeters", getMethod());
		}
	}

	protected JavaWriter getWriter() {
		return writer;
	}

	private boolean hasCorrectNumberOfParameters(final int numberOfParameters) {
		return numberOfParameters >= 0 && numberOfParameters <= 2;
	}

	protected abstract boolean hasCorrectReturnType(Annotateable method);

	public final void initialise(final JavaWriter writer, final FactoryType factoryType) {
		this.writer = writer;
		this.factoryType = factoryType;
	}

	protected boolean isReturnRequired() {
		return false;
	}

	protected final void setReceiverInvocationWriter(final ReceiverInvocationWriter receiverInvocationWriter) {
		this.receiverInvocationWriter = receiverInvocationWriter;
	}

	private void writeJavadoc() {
		getWriter()
			.println("/**")
			.println(" * @see " + method.getQualifiedNameOfDeclaringType() + "#" + method.getOriginalName())
			.println(" */");
	}

	private void startMethod() {
		factoryType.writeMethodModifiers(writer);
		getWriter().print(" ");
		method.writeGenericTypeParameters(writer);
		getWriter().print(" ");
		writeClosureDeclaration();
		getWriter().print((" " + getFactoryMethodName() + "("));
		writeFactoryMethodFormalParameters();
		getWriter().print(")");
	}

	public final void writeClosureDeclaration() {
		getWriter().print(getReturnType().getName());
		if (!getExecuteMethodParameters().isEmpty()) {
			getWriter().print('<');
			writeClosureTypes();
			getWriter().print('>');
		}
	}

	protected void writeClosureTypes() {
		getWriter().printBoxedCommaSeparatedList(getExecuteMethodParameters());
	}
	
	private void writeEqualsClosureMethod() {
		if (getFactoryMethodParameters().isEmpty()) {
			return;
		}
		getWriter()
			.print("private boolean equalsParameters(")
			.printFormalParameters(collect(getFactoryMethodParameters(), createAttributeNamePrefixingFunctor("$")), false)
			.print(")").openBlock()
				.print("return ")
				.print(join(collect(getFactoryMethodParameters(), createFactoryMethodParameterEqualityFunctor("$")), " && "))
				.println(";")
			.closeBlock();
	}

	private void writeEqualsMethod() {
		getWriter()
			.print("public boolean equals(Object obj)").openBlock()
				.println("if (obj == this) { return true; }")
				.println("if (!(obj instanceof Closure)) { return false; }");

		if (getFactoryMethodParameters().isEmpty()) {
			getWriter().println("return true;");
		} else {
			getWriter()
				.print(("return ((Closure) obj).equalsParameters(" + join(collect(getFactoryMethodParameters(), new AttributeNameFunctor()), ", ") + ")"))
				.println(";");
		}

		getWriter().closeBlock();
	}

	private void writeExecuteMethod() {
		getWriter()
			.print("public ")
			.print(isReturnRequired() ? getExecuteMethodReturnType() : "void")
			.print(" execute(")
			.printBoxedFormalParameters(getExecuteMethodParameters(method), false)
			.print(")").openBlock();
		receiverInvocationWriter.write(method, writer, isReturnRequired(), getExecuteMethodInvocationAttributeNameFunctor());
		getWriter().closeBlock();
	}

	protected abstract Functor<Attribute, String> getExecuteMethodInvocationAttributeNameFunctor();

	public final void writeFactoryMethodActualParameters(Functor<Attribute, String> attributeNameFunctor) {
		getWriter().printSimpleNamesAsActualParameterListWithoutBrackets(getFactoryMethodParameters(), false, attributeNameFunctor);
	}

	private void writeFactoryMethodFormalParameters() {
		getWriter().printFormalParameters(getFactoryMethodParameters(), false);
	}

	public void writeGenericTypeParameters() {
		method.writeGenericTypeParameters(writer);
	}

	private void writeHashCodeMethod() {
		getWriter()
			.print("public int hashCode()").openBlock()
				.print("return ")
				.print(fold("17", getFactoryMethodParameters(), createHashCodeFoldFunctor()))
				.println(";")
			.closeBlock();
	}

	public final void writeLocalClass(final String name) {
		getWriter().print("class " + name + " implements java.io.Serializable, ");
		writeClosureDeclaration();
		getWriter().openBlock();
		
		writeExecuteMethod();
		getWriter().println();
		
		writeHashCodeMethod();
		getWriter().println();
		
		writeEqualsMethod();
		getWriter().println();
		
		writeEqualsClosureMethod();

		getWriter().closeBlock();
	}
}