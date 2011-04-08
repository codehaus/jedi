package jedi.filters;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class NotNullFilterTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testReturnsTrueWhenArgumentIsNotNull() throws Exception {
		assertTrue(new NotNullFilter().execute("anything"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReturnsFalseWhenArgumentIsNull() throws Exception {
		assertFalse(new NotNullFilter().execute(null));
	}
}
