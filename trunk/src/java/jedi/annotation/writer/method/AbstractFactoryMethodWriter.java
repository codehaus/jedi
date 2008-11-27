package jedi.annotation.writer.method;

import static jedi.functional.FunctionalPrimitives.append;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.fold;
import static jedi.functional.FunctionalPrimitives.join;

import java.util.Collection;
import java.util.List;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.util.BoxerFunctor;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.factory.FactoryWriterException;
import jedi.annotation.writer.factorytype.ClosureFragmentWriter;
import jedi.annotation.writer.factorytype.FactoryType;
import jedi.annotation.writer.method.receiver.ReceiverInvocationWriter;
import jedi.functional.Functor;
import jedi.functional.Functor2;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.TypeMirror;

public abstract class AbstractFactoryMethodWriter implements ClosureFragmentWriter, FactoryMethodWriter {
	public static final String RECEIVER_PARAMETER_NAME = "$receiver";

	private JavaWriter writer;
	private FactoryType factoryType;
	private Annotateable method;
	private AnnotationProcessorEnvironment environment;

	private ReceiverInvocationWriter receiverInvocationWriter = new ReceiverInvocationWriter();

	public AbstractFactoryMethodWriter(AnnotationProcessorEnvironment environment) {
		this.environment = environment;
	}

	public final boolean canHandle(final Annotateable method) {
		return hasCorrectNumberOfParameters(getExecuteMethodParameters(method).size()) && hasCorrectReturnType(method);
	}

	protected final void checkPrecondition(boolean precondition, final String message) {
		if (!precondition) {
			throw new FactoryWriterException(message, method);
		}
	}

	private Functor<Attribute, String> createFieldEqualityFunctor() {
		return new Functor<Attribute, String>() {
			public String execute(final Attribute attribute) {
				final String field = getCorrespondingFieldName(attribute);
				return "(" + field + " == null ? that." + field + " == null : " + field + ".equals(that." + field + "))";
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

	protected final String getBoxedQualifiedTypeName(final TypeMirror type) {
		return new BoxerFunctor().execute(type);
	}

	private String getCorrespondingFieldName(final Attribute attribute) {
		return getCorrespondingFieldName(attribute.getName());
	}

	protected String getCorrespondingFieldName(final String parameterName) {
		return "$" + parameterName;
	}

	protected final TypeDeclaration getDelegateMethodDeclaringType() {
		return method.getDeclaringType();
	}

	protected final TypeMirror getDelegateMethodReturnType() {
		return method.getType();
	}

	protected final List<Attribute> getExecuteMethodParameters() {
		return getExecuteMethodParameters(getMethod());
	}

	protected abstract List<Attribute> getExecuteMethodParameters(Annotateable method);

	protected String getExecuteMethodReturnType() {
		return new BoxerFunctor().execute(getDelegateMethodReturnType());
	}

	protected abstract Collection<Attribute> getFactoryMethodAdditionalFormalParameters();

	protected abstract Collection<Attribute> getFactoryMethodBasicParameters();

	public final String getFactoryMethodName() {
		return getStartOfMethodName() + getFactoryMethodNameRequiredSuffix()
				+ optional("-AjediSuppressSuffixes", "", getFactoryMethodNameReturnTypeSuffix());
	}

	protected String optional(String option, String presentValue, String absentValue) {
		return isOption(option) ? presentValue : absentValue;
	}

	private String getStartOfMethodName() {
		return method.getName(isOption("-AjediSuppressAccessorVerbs"));
	}

	private boolean isOption(String key) {
		return environment.getOptions().containsKey(key);
	}

	protected String getFactoryMethodNameRequiredSuffix() {
		return "";
	}

	protected String getFactoryMethodNameReturnTypeSuffix() {
		return optional("-AjediSuppressClosureTypeSuffix", "", getReturnType().getSimpleName());
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
		println("\t * @see " + method.getDeclaringType().getQualifiedName() + "#" + method.getName(false));
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

	private void writeClosureField(final Attribute attribute) {
		print("\tprivate final ");
		print(attribute.getBoxedType());
		print(" ");
		print(getCorrespondingFieldName(attribute));
		print(" = ");
		print(attribute.getName());
		println(";");
	}

	private void writeClosureFields() {
		for (final Attribute attribute : getFactoryMethodParameters()) {
			writeClosureField(attribute);
		}
	}

	protected void writeClosureTypes() {
		getWriter().printBoxedCommaSeparatedList(getExecuteMethodParameters());
	}

	private void writeEqualsMethod() {
		println("\tpublic boolean equals(Object obj) {");
		println("\t\tif (obj == this) { return true; }");
		println("\t\tif (!(obj instanceof Closure)) { return false; }");

		if (getFactoryMethodParameters().isEmpty()) {
			println("\t\treturn true;");
		} else {
			println("\t\tClosure that = (Closure) obj;");
			print("\t\treturn ");
			print(join(collect(getFactoryMethodParameters(), createFieldEqualityFunctor()), " && "));
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
		writeClosureFields();
		writeExecuteMethod();
		writeEqualsMethod();
		writeHashCodeMethod();
		getWriter().println("}");
	}
}