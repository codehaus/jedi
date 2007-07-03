package jedi;

/**
 * Used by closure factories created from annotations to transform checked exceptions into
 * runtime exceptions. The only reason this type of exception should be thrown is to
 * wrap a checked exception thrown by a method which is executed by an implements of 
 * one of the Jedi closure types
 */
public class JediException extends RuntimeException {
    public JediException(Exception chained) {
        super("Caught and transformed exception", chained);
    }
}
