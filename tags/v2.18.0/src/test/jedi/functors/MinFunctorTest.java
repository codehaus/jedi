package jedi.functors;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class MinFunctorTest {
	@Test
	public void testExecuteReturnsFirstArgumentIfItIsTheMinimum() {
		verify(1, 2, 1);
	}

	@Test
	public void testExecuteReturnsSecondArgumentIfItIsTheMinimum() {
		verify(1, 1, 2);
	}

	private <T extends Comparable<T>> void verify(T expected, T first, T second) {
		assertEquals(expected, new MinFunctor<T>().execute(first, second));
	}
}
