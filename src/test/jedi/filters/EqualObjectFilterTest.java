package jedi.filters;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class EqualObjectFilterTest {
	private final Random random = new Random();
	private final long toFind = random.nextLong();

	@Test
	public void testExecuteReturnsFalseIfParameterIsNotEqual() {
		assertFalse(EqualObjectFilter.create(toFind).execute(random.nextLong()));
	}

	@Test
	public void testExecuteReturnsTrueIfParameterIsEqual() {
		assertTrue(EqualObjectFilter.create(toFind).execute(toFind));
	}
}
