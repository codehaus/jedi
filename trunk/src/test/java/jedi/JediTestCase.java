package jedi;

import jedi.functional.Functor;
import jedi.functional.Functor0;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public abstract class JediTestCase extends MockObjectTestCase {

    protected Functor mockFunctor(Object expectedArgument, Object result) {
        Mock mock = mock(Functor.class);
        mock.stubs().method("execute").with(eq(expectedArgument)).will(returnValue(result));
        return (Functor) mock.proxy();
    }

    protected Functor0 mockFunctor0(String constant) {
        Mock f0 = mock(Functor0.class);
        f0.stubs().method("execute").will(returnValue(constant));
        return (Functor0) f0.proxy();
    }
}
