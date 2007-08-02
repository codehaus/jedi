package jedi.filters;

import junit.framework.TestCase;

public class NotNullFilterTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testReturnsTrueWhenArgumentIsNotNull() throws Exception {
		assertTrue(new NotNullFilter().execute("anything"));
	}

	@SuppressWarnings("unchecked")
	public void testReturnsFalseWhenArgumentIsNull() throws Exception {
		assertFalse(new NotNullFilter().execute(null));
	}
}
