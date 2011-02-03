package jedi.either;

import junit.framework.Assert;
import org.junit.Test;

import static jedi.either.Either.cond;
import static junit.framework.Assert.assertEquals;

public class EitherTest {
    @Test
    public void testCondTrue() throws Exception {
        assertEquals(new Left<String, String>("a"), cond(true, "a", "b"));
    }

    @Test
    public void testCondFalse() throws Exception {
        assertEquals(new Right<String, String>("b"), cond(false, "a", "b"));
    }
}
