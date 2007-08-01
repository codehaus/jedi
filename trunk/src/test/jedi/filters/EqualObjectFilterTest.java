package jedi.filters;

import java.util.Random;

import junit.framework.TestCase;

public class EqualObjectFilterTest extends TestCase {
    private final Random random = new Random();
    private final long toFind = random.nextLong();

    public void testExecuteReturnsFalseIfParameterIsNotEqual() {
        assertFalse(EqualObjectFilter.create(toFind).execute(random.nextLong()));
    }

    public void testExecuteReturnsTrueIfParameterIsEqual() {
        assertTrue(EqualObjectFilter.create(toFind).execute(toFind));
    }
}