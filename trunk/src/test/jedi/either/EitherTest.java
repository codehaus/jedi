package jedi.either;

import jedi.functional.Functor;
import jedi.functional.Functor0;
import junit.framework.Assert;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.junit.Test;

import static jedi.either.Either.cond;
import static junit.framework.Assert.assertEquals;

public class EitherTest extends MockObjectTestCase {
    @Test
    public void testCondTrue() {
        assertEquals(new Left<String, String>("a"), cond(true, "a", "b"));
    }

    @Test
    public void testCondFalse() {
        assertEquals(new Right<String, String>("b"), cond(false, "a", "b"));
    }

    @Test
    public void testCondTrueWithFunctor() {
        Mock left = mock(Functor0.class);
        Mock right = mock(Functor0.class);

        left.expects(once()).method("execute");

        cond(true, (Functor0) left.proxy(), (Functor0) right.proxy());
    }

    @Test
    public void testCondFalseWithFunctor() {
        Mock left = mock(Functor0.class);
        Mock right = mock(Functor0.class);

        right.expects(once()).method("execute");

        cond(false, (Functor0) left.proxy(), (Functor0) right.proxy());
    }
}