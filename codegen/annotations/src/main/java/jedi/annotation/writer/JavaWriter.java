package jedi.annotation.writer;

import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.join;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import jedi.annotation.processor.model.Attribute;
import jedi.annotation.processor.model.AttributeBoxedTypeFunctor;
import jedi.annotation.processor.model.AttributeNameFunctor;
import jedi.functional.Command;

public class JavaWriter {
	private final PrintWriter writer;
	private int indentation;
	private boolean atStartOfLine = true;
	
	public JavaWriter(Writer delegate) {
		writer = new PrintWriter(delegate);
	}
	
	public JavaWriter openBlock() {
		return println(" {").indent();
	}
	
	public JavaWriter closeBlock() {
		return unindent().println('}');
	}
	
	private JavaWriter indent() {
		indentation++;
		return this;
	}
	
	private JavaWriter unindent() {
		indentation--;
		return this;
	}
	
	public JavaWriter print(char c) {
		print(Character.toString(c));
		return this;
	}
	
	public JavaWriter println(char c) {
		print(c);
		println();
		return this;
	}

	public JavaWriter print(String s) {
		doIndentation();
		writer.print(s);
		return this;
	}

	public JavaWriter println(String s) {
		print(s);
		println();
		return this;
	}
	
	public JavaWriter println() {
		writer.println();
		atStartOfLine = true;
		return this;
	}
	
	public void close() {
		writer.close();
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

	public void printFormalParameter(Attribute v) {
		printFormalParameter(v.getType(), v.getName());
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

	private void doIndentation() {
		if (!atStartOfLine) {
			return;
		}
		for (int i = 0 ; i < indentation ; i++) {
			writer.print('\t');
		}
		atStartOfLine = false;
	}
}
