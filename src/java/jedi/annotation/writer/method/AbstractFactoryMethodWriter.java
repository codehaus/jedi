package jedi.annotation.writer.method;

import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.List;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.util.BoxerFunctor;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.factory.FactoryWriterException;
import jedi.annotation.writer.factorytype.ClosureFragmentWriter;
import jedi.annotation.writer.factorytype.FactoryType;
import jedi.annotation.writer.method.receiver.ReceiverInvocationWriter;
import jedi.functional.FunctionalPrimitives;
import jedi.functional.Functor;
import jedi.functional.Functor2;

import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.TypeMirror;

public abstract class AbstractFactoryMethodWriter implements ClosureFragmentWriter, FactoryMethodWriter {
    public static final String RECEIVER_PARAMETER_NAME = "$receiver";

    private JavaWriter writer;
    private FactoryType factoryType;
    private JediMethod method;

    private ReceiverInvocationWriter receiverInvocationWriter = new ReceiverInvocationWriter();

    public final void initialise(JavaWriter writer, FactoryType factoryType) {
        this.writer = writer;
        this.factoryType = factoryType;
    }

    public final void execute(JediMethod method) {
        this.method = method;

        startMethod();
        factoryType.writeMethodBody(this, writer);

        this.method = null;
    }

    protected abstract List<Attribute> getExecuteMethodParameters(JediMethod method);

    protected abstract Collection<Attribute> getFactoryMethodBasicParameters();

    protected abstract Collection<Attribute> getFactoryMethodAdditionalFormalParameters();

    protected final List<Attribute> getExecuteMethodParameters() {
        return getExecuteMethodParameters(getMethod());
    }

    protected final void checkPrecondition(boolean precondition, String message) {
        if (!precondition) {
            throw new FactoryWriterException(message, method);
        }
    }

    protected JavaWriter getWriter() {
        return writer;
    }

    protected JediMethod getMethod() {
        return method;
    }

    protected final TypeMirror getDelegateMethodReturnType() {
        return method.getReturnType();
    }

    protected final TypeDeclaration getDelegateMethodDeclaringType() {
        return method.getDeclaringType();
    }

    public final String getFactoryMethodName() {
        return method.getName() + getFactoryMethodNameSuffix();
    }

    protected String getFactoryMethodNameSuffix() {
        return getReturnType().getSimpleName();
    }

    public final void writeClosureDeclaration() {
        writer.print(getReturnType().getName());
        writer.print('<');
        writeClosureTypes();
        writer.print('>');
    }

    private void startMethod() {
        print("\t");
        factoryType.writeMethodModifiers(writer);
        print(" ");
        writeGenericTypeParameters();
        print(" ");
        writeClosureDeclaration();
        print(" " + getFactoryMethodName() + "(");
        writeFactoryMethodFormalParameters();
        print(")");
    }

    private void writeGenericTypeParameters() {
        if (method.isGeneric()) {
            print("<");
            print(FunctionalPrimitives.join(method.getGenericTypeParameters(), ", "))
;            print(">");
        }
    }

    private void writeFactoryMethodFormalParameters() {
        getWriter().printFormalParameters(getFactoryMethodParameters(), false);
    }

    @SuppressWarnings("unchecked")
    protected List<Attribute> getFactoryMethodParameters() {
        return append(getFactoryMethodAdditionalFormalParameters(), getFactoryMethodBasicParameters());
    }

    protected boolean isReturnRequired() {
        return false;
    }

    protected final String getBoxedQualifiedTypeName(TypeMirror type) {
        return new BoxerFunctor().execute(type);
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
    
    public final void writeLocalClass(String name) {
        getWriter().print("class " + name + " implements java.io.Serializable, ");
        writeClosureDeclaration();
        getWriter().println(" {");
        writeClosureFields();
        writeExecuteMethod();
        writeEqualsMethod();
        writeHashCodeMethod();
        getWriter().println("}");
    }

    private void writeHashCodeMethod() {
        println("\tpublic int hashCode() {");
        print("\t\treturn ");
        print(fold("17", getFactoryMethodParameters(), createHashCodeFoldFunctor()));
        println(";");
        println("\t}");
    }

    private Functor2<String, Attribute, String> createHashCodeFoldFunctor() {
        return new Functor2<String, Attribute, String>() {
            public String execute(String accumulator, Attribute attribute) {
                String field = getCorrespondingFieldName(attribute);
                return "(" + accumulator + ") * 37 + (" + field + " == null ? 0 : " + field + ".hashCode())";
            }
        };
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

    private Functor<Attribute, String> createFieldEqualityFunctor() {
        return new Functor<Attribute, String>() {
            public String execute(Attribute attribute) {
               String field = getCorrespondingFieldName(attribute);
               return "(" + field + " == null ? that." + field + " == null : " + field + ".equals(that." + field + "))";
            }
        };
    }

    private void writeClosureFields() {
        for (Attribute attribute : getFactoryMethodParameters()) {
            writeClosureField(attribute);
        }
    }

    private void writeClosureField(Attribute attribute) {
        print("\tprivate final ");
        print(attribute.getBoxedType());
        print(" ");
        print(getCorrespondingFieldName(attribute));
        print(" = ");
        print(attribute.getName());
        println(";");
    }

    private String getCorrespondingFieldName(Attribute attribute) {
        return "$" + attribute.getName();
    }

    protected String getExecuteMethodReturnType() {
        return new BoxerFunctor().execute(getDelegateMethodReturnType());
    }
    
    protected final void setReceiverInvocationWriter(ReceiverInvocationWriter receiverInvocationWriter) {
        this.receiverInvocationWriter = receiverInvocationWriter;
    }

    public final void writeFactoryMethodActualParameters() {
        getWriter().printSimpleNamesAsActualParameterListWithoutBrackets(getFactoryMethodParameters(), false);
    }

    public final boolean canHandle(JediMethod method) {
        return hasCorrectNumberOfParameters(getExecuteMethodParameters(method).size()) && hasCorrectReturnType(method);
    }

    protected abstract boolean hasCorrectReturnType(JediMethod method);

    private boolean hasCorrectNumberOfParameters(int numberOfParameters) {
        return numberOfParameters >= 1 && numberOfParameters <= 2;
    }

    private Class<?> getReturnType() {
        int n = getExecuteMethodParameters().size();
        try {
            return n == 1 ? getOneParameterClosureClass() : Class.forName(getOneParameterClosureClass().getName() + n);
        } catch (ClassNotFoundException e) {
            throw new FactoryWriterException("No appropriate closure for method with " + n + " pararmeters", getMethod());
        }
    }
    
    protected abstract Class<?> getOneParameterClosureClass();

    protected void writeClosureTypes() {
        getWriter().printBoxedCommaSeparatedList(getExecuteMethodParameters());
    }
    
    private void println(String s) {
        getWriter().println(s);
    }
    
    private void print(String s) {
        getWriter().print(s);
    }
}