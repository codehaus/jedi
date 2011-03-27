package jedi.filters;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public abstract class LogicTestCase extends MockObjectTestCase {

	protected void returnValue(Mock filter, boolean value) {
		filter.stubs().method("execute").with(ANYTHING).will(returnValue(value));
	}

}