package jedi.functors;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class MaxFunctorTest {
	@Test
	public void testExecuteReturnsFirstArgumentIfItIsTheMaximum() {
		verify(2, 2, 1);
	}

	@Test
	public void testExecuteReturnsSecondArgumentIfItIsTheMaximum() {
		verify(2, 1, 2);
	}

	private <T extends Comparable<T>> void verify(T expected, T first, T second) {
		assertEquals(expected, new MaxFunctor<T>().execute(first, second));
	}
}
