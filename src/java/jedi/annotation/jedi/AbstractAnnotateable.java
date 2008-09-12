package jedi.annotation.jedi;

import jedi.annotation.util.BooleanTypeMirrorFilter;
import jedi.annotation.util.VoidTypeMirrorFilter;
import jedi.annotation.writer.method.FactoryMethodWriter;

import com.sun.mirror.declaration.MemberDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.util.SourcePosition;


abstract class AbstractAnnotateable<T extends MemberDeclaration> implements Annotateable {
    private final FactoryMethodWriter factoryMethodWriter;
    protected String name;
	protected final T declaration;
    
    public AbstractAnnotateable(T declaration, FactoryMethodWriter writer, String name) {
		this.declaration = declaration;
		factoryMethodWriter = writer;
		this.name = (name == null || name.length() == 0 ? declaration.getSimpleName() : name);
    }
    
    public void writeFactoryMethod() {
        factoryMethodWriter.execute(this);
    }

	@Override
	public int hashCode() {
	    return declaration.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }

        AbstractAnnotateable<?> that = (AbstractAnnotateable<?>) obj;
        return declaration.equals(that.declaration) && factoryMethodWriter.equals(that.factoryMethodWriter)
            && name.equals(that.name);
	}

	protected String getOriginalName() {
	    return declaration.getSimpleName();
	}

	public SourcePosition getPosition() {
	    return declaration.getPosition();
	}

	public boolean isVoid() {
	    return new VoidTypeMirrorFilter().execute(getType());
	}

	public boolean isBoolean() {
	    return new BooleanTypeMirrorFilter().execute(getType());
	}

	public TypeDeclaration getDeclaringType() {
	    return declaration.getDeclaringType();
	}
}
