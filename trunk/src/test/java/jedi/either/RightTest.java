package jedi.either;

import java.util.Collections;

import jedi.functional.Functor;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

public class RightTest extends MockObjectTestCase {

	private final Right<String, Integer> right = new Right<String, Integer>(1);

	@Test
	public void testIsRight() {
		assertTrue(right.isRight());
		assertFalse(right.isLeft());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFold() {
		Mock fa= mock(Functor.class);
		Mock fb = mock(Functor.class);
		fb.expects(once()).method("execute").with(eq(1)).will(returnValue(5));

		assertEquals(5, right.fold((Functor) fa.proxy(), (Functor) fb.proxy()));
	}

	@Test
	public void testSwap() {
		assertEquals(new Left<Integer, String>(1), right.swap());
	}

	@Test
	public void testAsList() {
		assertEquals(Collections.emptyList(), right.asList());
	}

	@Test
	public void testMap() {
		Mock fa= mock(Functor.class);
		fa.expects(never()).method("execute");

		assertEquals(new Right(1), right.map((Functor<String, Integer>) fa.proxy()));
	}

	@Test
	public void testFlatMap() {
		Mock fa = mock(Functor.class);
		fa.expects(never()).method("execute");

		assertEquals(new Right(1), right.flatMap((Functor<String, Either<String, Integer>>) fa.proxy()));
	}
}
