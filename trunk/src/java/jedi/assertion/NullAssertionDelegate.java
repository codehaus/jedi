package jedi.assertion;

public class NullAssertionDelegate implements AssertionDelegate {
    
    public void assertTrue(boolean value, String message, Object... context) {
        // No-op
    }
}
