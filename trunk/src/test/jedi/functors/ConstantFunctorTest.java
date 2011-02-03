package jedi.functors;

import jedi.option.Option;
import jedi.option.Options;
import org.junit.Test;

import static jedi.functors.ConstantFunctor.constant;
import static jedi.option.Options.some;
import static junit.framework.Assert.assertEquals;

public class ConstantFunctorTest {
    
    @Test
    public void testConstant() throws Exception {
        assertEquals("a", constant("a").execute(3));
    }

    @Test
    public void mappingToTestGenerics() {
        Option<String> o = some("foo");
        assertEquals(some("a"), o.map(constant("a")));
    }
}
