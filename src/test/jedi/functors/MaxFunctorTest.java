package jedi.functors;

import junit.framework.TestCase;

public class MaxFunctorTest extends TestCase {
    public void testExecuteReturnsFirstArgumentIfItIsTheMaximum() {
        verify(2, 2, 1);
    }
    
    public void testExecuteReturnsSecondArgumentIfItIsTheMaximum() {
        verify(2, 1, 2);
    }
    
    private <T extends Comparable<T>> void verify(T expected, T first, T second) {
        assertEquals(expected, new MaxFunctor<T>().execute(first, second));
    }
}
