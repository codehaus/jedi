package jedi.annotation.processor.model;

import static jedi.functional.FirstOrderLogic.invert;
import static jedi.functional.FunctionalPrimitives.select;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jedi.annotation.writer.JavaWriter;
import jedi.functional.Filter;
import jedi.functional.Functor;

public class JediMethod extends AbstractAnnotateable {
	private final Set<String> cutParameterNames;

	public JediMethod(MemberDeclaration declaration, Class<?> annotationClass) {
		this(declaration, annotationClass, null);
	}

	public JediMethod(MemberDeclaration declaration, Class<?> annotationClass, String factoryMethodPrefix) {
		this(declaration, annotationClass, factoryMethodPrefix, null);
	}

	public JediMethod(MemberDeclaration declaration, Class<?> annotationClass, String cutName, Set<String> cutParameterNames) {
		super(declaration, annotationClass, cutName);
		this.cutParameterNames = (cutParameterNames == null ? new HashSet<String>() : cutParameterNames);
	}

	public List<Attribute> getUncutParameters() {
		return selectParameters(invert(createCutParameterFilter()));
	}

	public List<Attribute> getCutParameters() {
		return selectParameters(createCutParameterFilter());
	}

	private List<Attribute> selectParameters(final Filter<Attribute> filter) {
		return select(declaration.getParameters(), filter);
	}

	private Filter<Attribute> createCutParameterFilter() {
		return new Filter<Attribute>() {
			public Boolean execute(Attribute value) {
				return cutParameterNames.contains(value.getName());
			}
		};
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && ((JediMethod) obj).cutParameterNames.equals(cutParameterNames);
	}

	private String removeNamePrefix(String prefix) {
		return Character.toString(Character.toLowerCase(name.charAt(prefix.length()))) + name.substring(prefix.length() + 1);
	}

	private boolean isNamePrefixedWith(String prefix) {
		return name.startsWith(prefix) && name.length() > prefix.length() && Character.isUpperCase(name.charAt(prefix.length()));
	}

	public void writeInvocation(JavaWriter writer, String receiverName, Functor<Attribute, String> attributeNameFunctor) {
		writer.print(receiverName + "." + getOriginalName());
		writer.printSimpleNamesAsActualParameterList(declaration.getParameters(), attributeNameFunctor);
	}

	public String getName(boolean simplified) {
		return simplified ? getSimplifiedName() : name;
	}

	private String getSimplifiedName() {
		if (isNamePrefixedWith("get")) {
			return removeNamePrefix("get");
		}
		if (isNamePrefixedWith("is")) {
			return removeNamePrefix("is");
		}
		return name;
	}

	public void writeGenericTypeParameters(JavaWriter writer) {
		writer.print(declaration.renderGenericTypeParameters());
	}
}
