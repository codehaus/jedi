package jedi.functional;

import org.junit.Test;

import static jedi.functional.Coercions.asList;
import static jedi.functional.Coercions.list;
import static org.junit.Assert.assertEquals;

public class RangeTest {

    @Test
    public void testIterator() {
        assertEquals(list(1, 2, 3), asList(new Range(1,4)));
    }

    @Test
    public void testWithFilter() {
        assertEquals(list(0, 2, 4), asList(new Range(0, 5, new Evens())));
    }

    private class Evens implements Filter<Integer> {
        public Boolean execute(Integer value) {
            return value % 2 == 0;
        }
    }

}
