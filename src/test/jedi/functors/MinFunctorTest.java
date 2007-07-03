package jedi.functors;

import junit.framework.TestCase;

public class MinFunctorTest extends TestCase {
    public void testExecuteReturnsFirstArgumentIfItIsTheMinimum() {
        verify(1, 2, 1);
    }
    
    public void testExecuteReturnsSecondArgumentIfItIsTheMinimum() {
        verify(1, 1, 2);
    }
    
    private <T extends Comparable<T>> void verify(T expected, T first, T second) {
        assertEquals(expected, new MinFunctor<T>().execute(first, second));
    }
}
