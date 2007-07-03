package jedi.annotation.writer;

import jedi.assertion.Assert;

public class AssertNotNullWriter {
    public void write(JavaWriter printWriter, String variable) {
        printWriter.print(Assert.class.getName());
        printWriter.print(".assertNotNull(");
        printWriter.print(variable);
        printWriter.print(", \"");
        printWriter.print(variable);
        printWriter.print(" must not be null\"");
        printWriter.println(");");
    }
}
