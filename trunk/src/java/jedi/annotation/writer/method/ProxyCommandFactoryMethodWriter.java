package jedi.annotation.writer.method;

import jedi.annotation.jedi.JediMethod;
import jedi.functional.Command;

public class ProxyCommandFactoryMethodWriter extends AbstractProxyFactoryMethodWriter {
    @Override
    protected Class< ? > getOneParameterClosureClass() {
        return Command.class;
    }
    
    @Override
    protected boolean hasCorrectReturnType(JediMethod method) {
        return true;
    }
}
