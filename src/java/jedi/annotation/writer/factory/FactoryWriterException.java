package jedi.annotation.writer.factory;

import jedi.annotation.jedi.JediMethod;

import com.sun.mirror.apt.Messager;

public class FactoryWriterException extends RuntimeException {
    private JediMethod method;

    public FactoryWriterException(String message, JediMethod method) {
        super(message);
        this.method = method;
    }

    public void write(Messager messager) {
        messager.printError(method.getPosition(), getMessage());
    }
}
