package jedi.functional;

import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public abstract class ClosureTestCase extends MockObjectTestCase {
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
}