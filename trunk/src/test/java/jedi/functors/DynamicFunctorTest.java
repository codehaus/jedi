package jedi.functors;

import org.junit.Test;

import static jedi.functional.Coercions.list;
import static junit.framework.Assert.assertTrue;

public class DynamicFunctorTest {

    @Test
    public void testNoArgMethod() {
        DynamicFunctor<String, Integer> stringLength = new DynamicFunctor(String.class, "length");
        assertTrue(5 == stringLength.execute("hello"));
    }

    @Test
    public void testMethodWithArgs() {
        DynamicFunctor<String, Character> stringCharAt= new DynamicFunctor(String.class, "charAt");
        assertTrue('l' == stringCharAt.execute("hello", list(3)));
    }
}
