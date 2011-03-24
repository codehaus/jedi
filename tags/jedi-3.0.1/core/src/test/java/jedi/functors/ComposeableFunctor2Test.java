package jedi.functors;

import jedi.JediTestCase;
import jedi.functional.Functor;
import jedi.functional.Functor2;
import org.junit.Test;

import static jedi.functors.ComposeableFunctor.c;

public class ComposeableFunctor2Test extends JediTestCase {

    @Test
    public void testCompose() {
        Functor2 g = mockFunctor2("x", "y", "g result");
        Functor f = mockFunctor("g result", "f result");

        assertEquals("f result", c(g).andThen(f).execute("x", "y"));
    }
    
}