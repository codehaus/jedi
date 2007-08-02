package jedi.filters;

import jedi.functional.Filter;

import org.jmock.Mock;

public class DisjunctionTest extends LogicTestCase {

	private Mock filterA = mock(Filter.class);
	private Mock filterB = mock(Filter.class);

	@SuppressWarnings("unchecked")
	public void testReturnsTrueWhenAnyFiltersReturnTrue() throws Exception {
		returnValue(filterA, true);
		returnValue(filterB, false);
		assertTrue(new Disjunction((Filter) filterA.proxy(), (Filter) filterB.proxy()).execute("anything"));
	}

	@SuppressWarnings("unchecked")
	public void testReturnsFalseWhenAllFilterReturnsFalse() throws Exception {
		returnValue(filterA, false);
		returnValue(filterB, false);
		assertFalse(new Disjunction((Filter) filterA.proxy(), (Filter) filterB.proxy()).execute("anything"));
	}
}
