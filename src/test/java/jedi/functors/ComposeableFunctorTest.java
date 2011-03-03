package jedi.functors;

import jedi.JediTestCase;
import jedi.functional.Functor;
import jedi.functional.Functor0;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

import static jedi.functors.ComposeableFunctor.c;
import static jedi.functors.ComposeableFunctor.composeable;

public class ComposeableFunctorTest extends JediTestCase {

    @Test
    public void testCompose() {
        Functor g = mockFunctor("x", "g result");
        Functor f = mockFunctor("g result", "f result");

        assertEquals("f result", c(g).andThen(f).execute("x"));
    }

    @Test
    public void testO() {
        Functor g = mockFunctor("x", "g result");
        Functor f = mockFunctor("g result", "f result");

        assertEquals("f result", c(f).o(g).execute("x"));
    }

    @Test
    public void testCompositionWithFunctor0() {
        Functor0 f0 = mockFunctor0("constant");
        Functor f = mockFunctor("constant", "f result");

        assertEquals("f result", composeable(f).o(f0).execute());
    }

    @Test
    public void testTypesCompile() {
        class A {}
        class B {}
        class C extends B {}

        Functor<A, B> fab = (Functor<A, B>) mock(Functor.class).proxy();
        Functor<B, C> fbc = (Functor<B, C>) mock(Functor.class).proxy();
        Functor<A, C> fac = (Functor<A, C>) mock(Functor.class).proxy();

        Functor<A, C> simpleCase = c(fab).andThen(fbc);
        Functor<A, C> secondFunctorTakesSuperTypeOfFirstFunctorsReturnType = c(fac).andThen(fbc);
        
        Functor<A, C> simpleCase2 = c(fbc).o(fac);
        Functor<A, C> firstFunctorReturnsSubtypeOfSecondFunctorsInputType = c(fbc).o(fac);
    }

}
