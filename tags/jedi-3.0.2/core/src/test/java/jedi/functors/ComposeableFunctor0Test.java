package jedi.functors;

import jedi.JediTestCase;
import jedi.functional.Functor;
import jedi.functional.Functor0;
import org.junit.Test;

import static jedi.functors.ComposeableFunctor.composeable;
import static jedi.functors.ComposeableFunctor.c;

public class ComposeableFunctor0Test extends JediTestCase {

    @Test
    public void testExecution() {
        Functor0 f0 = mockFunctor0("constant");

        assertEquals("constant", composeable(f0).execute());
    }

    @Test
    public void testCompositionWithFunctor0() {
        Functor0 f0 = mockFunctor0("constant");
        Functor f = mockFunctor("constant", "f result");

        assertEquals("f result", c(f0).andThen(f).execute());
    }
}