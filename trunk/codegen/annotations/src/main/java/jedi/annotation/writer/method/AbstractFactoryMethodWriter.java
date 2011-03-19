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
				final String field = getCorrespondingFieldName(attribute);
				return "(" + prefix + field + " == null ? " + field + " == null : " + prefix + field + ".equals(" + field + "))";
			}
		};
	}
	
	private Functor<Attribute, Attribute> createAttributeNamePrefixingFunctor(final String prefix) {
		return new Functor<Attribute, Attribute>() {
			public Attribute execute(Attribute value) {
				return new Attribute(value.getBoxedType(), prefix + value.getName());
			}
		};
	}

	private Functor2<String, Attribute, String> createHashCodeFoldFunctor() {
		return new Functor2<String, Attribute, String>() {
			public String execute(final String accumulator, final Attribute attribute) {
				final String field = getCorrespondingFieldName(attribute);
				return "(" + accumulator + ") * 37 + (" + field + " == null ? 0 : " + field + ".hashCode())";
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

	private String getCorrespondingFieldName(final Attribute attribute) {
		return getCorrespondingFieldName(attribute.getName());
	}

	protected String getCorrespondingFieldName(final String parameterName) {
		return parameterName;
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

	private void print(final String s) {
		getWriter().print(s);
	}

	private void println(final String s) {
		getWriter().println(s);
	}

	protected final void setReceiverInvocationWriter(final ReceiverInvocationWriter receiverInvocationWriter) {
		this.receiverInvocationWriter = receiverInvocationWriter;
	}

	private void writeJavadoc() {
		println("\t/**");
		println("\t * @see " + method.getQualifiedNameOfDeclaringType() + "#" + method.getOriginalName());
		println("\t */");
	}

	private void startMethod() {
		print("\t");
		factoryType.writeMethodModifiers(writer);
		print(" ");
		method.writeGenericTypeParameters(writer);
		print(" ");
		writeClosureDeclaration();
		print(" " + getFactoryMethodName() + "(");
		writeFactoryMethodFormalParameters();
		print(")");
	}

	public final void writeClosureDeclaration() {
		writer.print(getReturnType().getName());
		if (!getExecuteMethodParameters().isEmpty()) {
			writer.print('<');
			writeClosureTypes();
			writer.print('>');
		}
	}

	protected void writeClosureTypes() {
		getWriter().printBoxedCommaSeparatedList(getExecuteMethodParameters());
	}
	
	private void writeEqualsClosureMethod() {
		if (getFactoryMethodParameters().isEmpty()) {
			return;
		}
		print("\tprivate boolean equalsParameters(");
		getWriter().printFormalParameters(collect(getFactoryMethodParameters(), createAttributeNamePrefixingFunctor("$")), false);
		println(") {");
		print("\t\t return ");
		print(join(collect(getFactoryMethodParameters(), createFactoryMethodParameterEqualityFunctor("$")), " && "));
		println(";");
		println("\t}");
	}

	private void writeEqualsMethod() {
		println("\tpublic boolean equals(Object obj) {");
		println("\t\tif (obj == this) { return true; }");
		println("\t\tif (!(obj instanceof Closure)) { return false; }");

		if (getFactoryMethodParameters().isEmpty()) {
			println("\t\treturn true;");
		} else {
			print("\t\treturn ((Closure) obj).equalsParameters(" + join(collect(getFactoryMethodParameters(), new AttributeNameFunctor()), ", ") + ")");
			println(";");
		}

		println("\t}");
	}

	private void writeExecuteMethod() {
		getWriter().print("public ");
		getWriter().print(isReturnRequired() ? getExecuteMethodReturnType() : "void");
		getWriter().print(" execute(");
		getWriter().printBoxedFormalParameters(getExecuteMethodParameters(method), false);
		getWriter().println(") {");
		receiverInvocationWriter.write(method, writer, isReturnRequired());
		getWriter().println("}");
	}

	public final void writeFactoryMethodActualParameters() {
		getWriter().printSimpleNamesAsActualParameterListWithoutBrackets(getFactoryMethodParameters(), false);
	}

	private void writeFactoryMethodFormalParameters() {
		getWriter().printFormalParameters(getFactoryMethodParameters(), false);
	}

	public void writeGenericTypeParameters() {
		method.writeGenericTypeParameters(writer);
	}

	private void writeHashCodeMethod() {
		println("\tpublic int hashCode() {");
		print("\t\treturn ");
		print(fold("17", getFactoryMethodParameters(), createHashCodeFoldFunctor()));
		println(";");
		println("\t}");
	}

	public final void writeLocalClass(final String name) {
		getWriter().print("class " + name + " implements java.io.Serializable, ");
		writeClosureDeclaration();
		getWriter().println(" {");
		writeExecuteMethod();
		writeEqualsClosureMethod();
		writeEqualsMethod();
		writeHashCodeMethod();
		getWriter().println("}");
	}
}