package jedi.functors;

import jedi.functional.Functor;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

import static jedi.functors.ComposeableFunctor.c;

public class ComposeableFunctorTest extends MockObjectTestCase {

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

    private Functor mockFunctor(Object expectedArgument, Object result) {
        Mock mock = mock(Functor.class);
        mock.expects(once()).method("execute").with(eq(expectedArgument)).will(returnValue(result));
        return (Functor) mock.proxy();
    }
}
