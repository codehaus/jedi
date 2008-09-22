package jedi.filters;

import jedi.functional.Filter;

import org.jmock.Mock;
import org.junit.Test;

public class InverterTest extends LogicTestCase {
	private Mock filter = mock(Filter.class);

	@SuppressWarnings("unchecked")
	@Test
	public void testInvertsTrue() throws Exception {
		returnValue(filter, true);
		assertFalse(new Inverter((Filter) filter.proxy()).execute("anything"));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInvertsFalse() throws Exception {
		returnValue(filter, false);
		assertTrue(new Inverter((Filter) filter.proxy()).execute("anything"));

	}
}
