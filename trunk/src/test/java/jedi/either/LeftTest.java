package jedi.either;

import static jedi.functional.Coercions.list;
import jedi.functional.Functor;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

public class LeftTest extends MockObjectTestCase {

	private final Left<String, Integer> left = new Left<String, Integer>("a");

    @Test
    public void testEqualsHashcodeWhenEqual() {
        Left<String, Integer> another = new Left<String, Integer>("a");
        assertEquals(left, another);
        assertEquals(left.hashCode(), another.hashCode());
    }

    @Test
    public void testEqualsWhenNotEqual() {
        assertFalse(left.equals(new Left<String, Integer>("b")));
    }

	@Test
	public void testIsLeft() {
		assertTrue(left.isLeft());
		assertFalse(left.isRight());
	}

	@Test
	public void testFold() {
		Mock fa= mock(Functor.class);
		Mock fb = mock(Functor.class);
		fa.expects(once()).method("execute").with(eq("a")).will(returnValue("ok"));

		assertEquals("ok", left.fold((Functor) fa.proxy(), (Functor) fb.proxy()));
	}

	@Test
	public void testSwap() {
		assertEquals(new Right<Integer, String>("a"), left.swap());
	}

	@Test
	public void testAsList() {
		assertEquals(list("a"), left.asList());
	}

	@Test
	public void testMap() {
		Mock fa = mock(Functor.class);
		fa.expects(once()).method("execute").with(eq("a")).will(returnValue("ok"));

		assertEquals(new Left("ok"), left.map((Functor<String, Integer>) fa.proxy()));
	}

	@Test
	public void testFlatMap() {
		Mock fa = mock(Functor.class);
		fa.expects(once()).method("execute").with(eq("a")).will(returnValue(new Left("fmap")));

		assertEquals(new Left("fmap"), left.flatMap((Functor<String, Either<String, Integer>>) fa.proxy()));
	}
}
