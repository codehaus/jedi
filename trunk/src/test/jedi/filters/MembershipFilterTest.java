package jedi.filters;

import java.util.Collection;

import jedi.functional.Coercions;
import junit.framework.TestCase;

public class MembershipFilterTest extends TestCase {
    public void testExecuteReturnsFalseWhenTheArgumentIsNotAMember() {
        verify(1, Coercions.set(2, 3), false);
    }
    
    public void testExecuteReturnsFalseWhenTheCollectionIsEmpty() {
        verify(2, Coercions.set(), false);
    }
    
    public void testExecuteReturnsTrueWhenTheArgumentIsAMember() {
        verify(3, Coercions.set(3, 4), true);
    }
    
    private <T> void verify(T value, Collection<? super T> values, Boolean expected) {
        assertEquals(expected, MembershipFilter.create(values).execute(value));
    }
}
