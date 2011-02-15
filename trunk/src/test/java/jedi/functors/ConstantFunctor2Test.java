package jedi.functors;

import org.junit.Test;

import java.util.Date;

import static jedi.functors.ConstantFunctor2.constant;
import static junit.framework.Assert.assertEquals;

public class ConstantFunctor2Test {
    @Test
    public void testConstant() throws Exception {
        assertEquals("a", constant("a").execute(3, new Date()));
    }
}
