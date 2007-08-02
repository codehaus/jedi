package jedi.filters;

import jedi.functional.Filter;

import org.jmock.Mock;

public class InverterTest extends LogicTestCase {
	private Mock filter = mock(Filter.class);

	@SuppressWarnings("unchecked")
	public void testInvertsTrue() throws Exception {
		returnValue(filter, true);
		assertFalse(new Inverter((Filter) filter.proxy()).execute("anything"));

	}

	@SuppressWarnings("unchecked")
	public void testInvertsFalse() throws Exception {
		returnValue(filter, false);
		assertTrue(new Inverter((Filter) filter.proxy()).execute("anything"));

	}
}
