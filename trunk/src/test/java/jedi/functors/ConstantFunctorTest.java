package jedi.functors;

import jedi.either.Either;
import jedi.either.Left;
import jedi.option.Option;
import jedi.option.Options;
import org.junit.Test;

import java.math.BigDecimal;

import static jedi.functors.ConstantFunctor.constant;
import static jedi.option.Options.some;
import static junit.framework.Assert.assertEquals;

public class ConstantFunctorTest {
    
    @Test
    public void testConstant() throws Exception {
        assertEquals("a", constant("a").execute(3));
    }

    @Test
    public void mappingToTestGenerics() {
        Option<Integer> o = some(3);
        assertEquals(some("a"), o.map(constant("a")));
    }

    @Test
    public void mappingEither() {
        Either<BigDecimal, String> either = new Left<BigDecimal, String>(new BigDecimal("3"));
        assertEquals("z", either.fold(constant("z"), constant("y")));
    }
}
