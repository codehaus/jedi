package jedi.annotation.jedi;

import static jedi.functional.Coercions.list;

import java.util.List;

import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.method.FactoryMethodWriter;

import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.type.TypeMirror;

public class JediField extends AbstractAnnotateable<FieldDeclaration> {
	public JediField(FieldDeclaration declaration, FactoryMethodWriter writer, String name) {
		super(declaration, writer, name);
	}

	public List<Attribute> getCutParameters() {
		return list();
	}

	public String getName(boolean simplified) {
		return declaration.getSimpleName();
	}

	public TypeMirror getType() {
		return declaration.getType();
	}

	public List<Attribute> getUncutParameters() {
		return list();
	}

	public void writeGenericTypeParameters(JavaWriter writer) {
	}

	public void writeInvocation(JavaWriter writer, String receiverName) {
		writer.print(receiverName + "." + getOriginalName());
	}
}
