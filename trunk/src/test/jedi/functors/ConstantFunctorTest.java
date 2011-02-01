package jedi.functors;

import org.junit.Test;

import static jedi.functors.ConstantFunctor.constant;
import static junit.framework.Assert.assertEquals;

public class ConstantFunctorTest {

    @Test
    public void testConstantReturned() {
        assertEquals("a", constant("a").execute());
    }
}
