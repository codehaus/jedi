package jedi.filters;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public abstract class LogicTestCase extends MockObjectTestCase {

	public LogicTestCase() {
		super();
	}

	public LogicTestCase(String arg0) {
		super(arg0);
	}

	protected void returnValue(Mock filter, boolean value) {
		filter.stubs().method("execute").with(ANYTHING).will(returnValue(value));
	}

}