package jedi.filters;

import static jedi.functional.Coercions.set;

import java.util.Collection;

import junit.framework.TestCase;

public class MembershipFilterTest extends TestCase {
    public void testExecuteReturnsFalseWhenTheArgumentIsNotAMember() {
        verify(1, set(2, 3), false);
    }
    
    public void testExecuteReturnsFalseWhenTheCollectionIsEmpty() {
        verify(2, set(), false);
    }
    
    public void testExecuteReturnsTrueWhenTheArgumentIsAMember() {
        verify(3, set(3, 4), true);
    }
    
    private <T> void verify(T value, Collection<? super T> values, Boolean expected) {
        assertEquals(expected, MembershipFilter.create(values).execute(value));
    }
}
