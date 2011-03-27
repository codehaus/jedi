package jedi.filters;

import static jedi.functional.Coercions.set;
import static junit.framework.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

public class MembershipFilterTest {
	@Test
	public void testExecuteReturnsFalseWhenTheArgumentIsNotAMember() {
		verify(1, set(2, 3), false);
	}

	@Test
	public void testExecuteReturnsFalseWhenTheCollectionIsEmpty() {
		verify(2, set(), false);
	}

	@Test
	public void testExecuteReturnsTrueWhenTheArgumentIsAMember() {
		verify(3, set(3, 4), true);
	}

	private <T> void verify(T value, Collection<? super T> values, Boolean expected) {
		assertEquals(expected, MembershipFilter.create(values).execute(value));
	}
}
