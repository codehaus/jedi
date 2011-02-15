package jedi.functors;

import org.junit.Test;

import static jedi.functors.ConstantFunctor0.constant;
import static junit.framework.Assert.assertEquals;

public class ConstantFunctor0Test {

    @Test
    public void testConstantReturned() {
        assertEquals("a", constant("a").execute());
    }
}
