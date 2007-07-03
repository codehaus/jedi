package jedi.annotation.writer.method;

import jedi.annotation.jedi.JediMethod;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.factorytype.FactoryType;

public interface FactoryMethodWriter {
    void initialise(JavaWriter writer, FactoryType factoryType);
    void execute(JediMethod method);
    boolean canHandle(JediMethod method);
}
