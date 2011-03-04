package jedi;

import jedi.functional.Functor;
import jedi.functional.Functor0;
import jedi.functional.Functor2;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.util.List;

public abstract class JediTestCase extends MockObjectTestCase {

    protected static final Integer INTEGER_ONE = 1;
	protected static final Double DOUBLE_TWO = 2.0;
	protected static final String FOO = "foo";
	protected static final String BAR = "bar";

	protected Mock mockFunctor = mock(Functor.class);
	@SuppressWarnings("unchecked")
	protected Functor functor = (Functor) mockFunctor.proxy();

	@SuppressWarnings("unchecked")
	protected Functor setUpFunctorExpectations(List in, List out) {
		for (int i = 0; i < in.size(); i++) {
			mockFunctor.expects(once()).method("execute").with(same(in.get(i))).will(returnValue(out.get(i)));
		}

		return (Functor) mockFunctor.proxy();
	}

    protected Functor2 mockFunctor2(Object expectedArgument1, Object expectedArgument2, Object result) {
        Mock mock = mock(Functor2.class);
        mock.stubs().method("execute").with(eq(expectedArgument1), eq(expectedArgument2)).will(returnValue(result));
        return (Functor2) mock.proxy();
    }

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
