package jedi.annotation.jedi;

import static jedi.functional.FirstOrderLogic.*;
import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jedi.annotation.jedi.attribute.Attribute;
import jedi.annotation.util.BooleanTypeMirrorFilter;
import jedi.annotation.util.VoidTypeMirrorFilter;
import jedi.annotation.writer.method.FactoryMethodWriter;
import jedi.functional.Filter;
import jedi.functional.Functor;

import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.util.SourcePosition;

public class JediMethod {
    private final MethodDeclaration method;
    private final FactoryMethodWriter factoryMethodWriter;
    private String name;
    private Set<String> cutParameterNames;

    public JediMethod(MethodDeclaration declaration, FactoryMethodWriter factoryMethodWriter) {
        this(declaration, factoryMethodWriter, null, null);
    }
    
    public JediMethod(MethodDeclaration declaration, FactoryMethodWriter factoryMethodWriter, String factoryMethodPrefix) {
        this(declaration, factoryMethodWriter, factoryMethodPrefix, null);
    }

    public JediMethod(MethodDeclaration declaration, FactoryMethodWriter factoryMethodWriter, String cutName, Set<String> cutParameterNames) {
        this.method = declaration;
        this.factoryMethodWriter = factoryMethodWriter;
        this.name = (cutName == null || cutName.length() == 0 ? declaration.getSimpleName() : cutName);
        this.cutParameterNames = (cutParameterNames == null ? new HashSet<String>() : cutParameterNames);
    }

    public TypeDeclaration getDeclaringType() {
        return method.getDeclaringType();
    }
    
    public void write() {
        factoryMethodWriter.execute(this);
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return method.getSimpleName();
    }

    public SourcePosition getPosition() {
        return method.getPosition();
    }

    public TypeMirror getReturnType() {
        return method.getReturnType();
    }

    public Collection<Attribute> getParameters() {
        return collect(
            method.getParameters(), new Functor<ParameterDeclaration, Attribute>() {
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
    
    public boolean isVoidReturnType() {
        return new VoidTypeMirrorFilter().execute(method.getReturnType());
    }
    
    public boolean isBooleanReturnType() {
        return new BooleanTypeMirrorFilter().execute(method.getReturnType());
    }
    
    public boolean isGeneric() {
        return !method.getFormalTypeParameters().isEmpty();
    }
    
    public Collection< ? > getGenericTypeParameters() {
        return method.getFormalTypeParameters();
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }

        JediMethod that = (JediMethod) obj;
        return method.equals(that.method) && factoryMethodWriter.equals(that.factoryMethodWriter)
            && name.equals(that.name) && cutParameterNames.equals(that.cutParameterNames);
    }
}
