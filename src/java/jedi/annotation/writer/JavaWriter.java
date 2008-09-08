package jedi.annotation.writer;

import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.join;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.jedi.attribute.AttributeBoxedTypeFunctor;
import jedi.annotation.jedi.attribute.AttributeNameFunctor;
import jedi.annotation.util.BoxerFunctor;
import jedi.functional.Command;

import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.TypeMirror;

public class JavaWriter extends PrintWriter {
    public JavaWriter(Writer delegate) {
        super(delegate);
    }

    public void printSimpleNamesAsActualParameterList(Collection<Attribute> vs) {
        print('(');
        printSimpleNamesAsActualParameterListWithoutBrackets(vs, false);
        print(')');
    }

    public void printSimpleNamesAsActualParameterListWithoutBrackets(Collection<Attribute> declarations, boolean requiresLeadingComma) {
        if (requiresLeadingComma && !declarations.isEmpty()) {
            print(", ");
        }
        print(join(collect(declarations, new AttributeNameFunctor()), ", "));
    }
    
    public void printCommaSeparatedList(List<String> list) {
        print(join(list, ", "));
    }
    
    public final void printFormalParameter(final String type, final String name) {
        print("final ");
        print(type);
        print(' ');
        print(name);
    }

    public void printFormalParameter(TypeDeclaration type, String parameterName) {
        printFormalParameter(type.getQualifiedName(), parameterName);
    }
    
    public void printFormalParameter(TypeMirror type, String name) {
        printFormalParameter(type.toString(), name);
    }
    
    public void printFormalParameter(Attribute v) {
        printFormalParameter(v.getType(), v.getName());
    }

    public void printQualifiedTypeName(TypeDeclaration type) {
        print(type.getQualifiedName());
    }
    
    public void printBoxedQualifiedTypeName(TypeMirror type) {
        print(getBoxedQualifiedTypeName(type));
    }

    private String getBoxedQualifiedTypeName(TypeMirror type) {
        return new BoxerFunctor().execute(type);
    }
    
    public void printBoxedFormalParameter(Attribute v) {
        printFormalParameter(v.getBoxedType(), v.getName());
    }
    
    public void printBoxedFormalParameters(Collection<Attribute> parameters, boolean requiresLeadingComma) {
        printFormalParameters(parameters, requiresLeadingComma, new Command<Attribute>() {
            public void execute(Attribute v) {
                printBoxedFormalParameter(v);
            }
        });
    }

    public void printFormalParameters(Collection<Attribute> parameters, boolean requiresLeadingComma) {
        printFormalParameters(parameters, requiresLeadingComma, new Command<Attribute>() {
            public void execute(Attribute v) {
                printFormalParameter(v);
            }
        });
    }
    
    private void printFormalParameters(Collection<Attribute> parameters, boolean requiresLeadingComma, Command<Attribute> command) {
    	boolean commaRequired = requiresLeadingComma;
        for (Attribute parameter : parameters) {
            if (commaRequired) {
                print(", ");
            }
            commaRequired = true;
            command.execute(parameter);
        }
    }
    
    public void printBoxedCommaSeparatedList(List<Attribute> vs) {
        printCommaSeparatedList(collect(vs, new AttributeBoxedTypeFunctor()));
    }
}
