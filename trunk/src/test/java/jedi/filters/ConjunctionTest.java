package jedi.filters;

import jedi.functional.Filter;

import org.jmock.Mock;
import org.junit.Test;

public class ConjunctionTest extends LogicTestCase {

	private Mock filterA = mock(Filter.class);
	private Mock filterB = mock(Filter.class);

	@SuppressWarnings("unchecked")
	@Test
	public void testReturnsTrueWhenAllFiltersReturnTrue() throws Exception {
		returnValue(filterA, true);
		returnValue(filterB, true);
		assertTrue(new Conjunction((Filter) filterA.proxy(), (Filter) filterB.proxy()).execute("anything"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReturnsFalseWhenAnyFilterReturnsFalse() throws Exception {
		returnValue(filterA, true);
		returnValue(filterB, false);
		assertFalse(new Conjunction((Filter) filterA.proxy(), (Filter) filterB.proxy()).execute("anything"));
	}

}
