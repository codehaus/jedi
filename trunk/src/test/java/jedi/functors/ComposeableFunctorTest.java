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

    private Functor mockFunctor(Object expectedArgument, Object result) {
        Mock mock = mock(Functor.class);
        mock.expects(once()).method("execute").with(eq(expectedArgument)).will(returnValue(result));
        return (Functor) mock.proxy();
    }
}
