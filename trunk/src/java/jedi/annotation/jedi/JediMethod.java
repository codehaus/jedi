package jedi.annotation.jedi;

import static jedi.functional.FirstOrderLogic.invert;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.join;
import static jedi.functional.FunctionalPrimitives.select;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.method.FactoryMethodWriter;
import jedi.functional.Filter;
import jedi.functional.Functor;

import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.type.TypeMirror;

public class JediMethod extends AbstractAnnotateable<MethodDeclaration> {
    private Set<String> cutParameterNames;

    public JediMethod(MethodDeclaration declaration, FactoryMethodWriter factoryMethodWriter) {
        this(declaration, factoryMethodWriter, null, null);
    }
    
    public JediMethod(MethodDeclaration declaration, FactoryMethodWriter factoryMethodWriter, String factoryMethodPrefix) {
        this(declaration, factoryMethodWriter, factoryMethodPrefix, null);
    }

    public JediMethod(MethodDeclaration declaration, FactoryMethodWriter factoryMethodWriter, String cutName, Set<String> cutParameterNames) {
    	super(declaration, factoryMethodWriter, cutName);
        this.cutParameterNames = (cutParameterNames == null ? new HashSet<String>() : cutParameterNames);
    }

    public TypeMirror getType() {
        return declaration.getReturnType();
    }

    private Collection<Attribute> getParameters() {
        return collect(
            declaration.getParameters(), new Functor<ParameterDeclaration, Attribute>() {
                public Attribute execute(ParameterDeclaration value) {
                    return new Attribute(value);
                }
            });
    }
    
    public List<Attribute> getUncutParameters() {
        return getSelectedParameters(invert(createCutParameterFilter()));
    }
    
    public List<Attribute> getCutParameters() {
        return getSelectedParameters(createCutParameterFilter());
    }

    private List<Attribute> getSelectedParameters(final Filter<Attribute> filter) {
        return select(getParameters(), filter);
    }

    private Filter<Attribute> createCutParameterFilter() {
        return new Filter<Attribute>() {
            public Boolean execute(Attribute value) {
                return cutParameterNames.contains(value.getName());
            }
        };
    }
    
    private boolean isGeneric() {
        return !getGenericTypeParameters().isEmpty();
    }
    
    private Collection< ? > getGenericTypeParameters() {
        return declaration.getFormalTypeParameters();
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
	
	public void writeInvocation(JavaWriter writer, String receiverName) {
		writer.print(receiverName + "." + getOriginalName());
		writer.printSimpleNamesAsActualParameterList(getParameters());
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
        if (isGeneric()) {
            writer.print('<');
            writer.print(join(getGenericTypeParameters(), ", "));
            writer.print('>');
        }
    }
}
