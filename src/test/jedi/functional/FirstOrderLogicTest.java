package jedi.functional;

import static jedi.functional.FirstOrderLogic.*;

import java.util.List;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class FirstOrderLogicTest extends MockObjectTestCase {

	private Mock	mockPredicate	= mock(Filter.class);
	@SuppressWarnings("unchecked")
    private Filter	predicate		= (Filter) mockPredicate.proxy();
	private List<?>	predicated		= Coercions.list(new Object(), new Object());

	@SuppressWarnings("unchecked")
	public void testExistsReturnsFalseWithEmptyCollection() {
		assertFalse(exists(Coercions.set(), predicate));
	}

	@SuppressWarnings("unchecked")
	public void testExistsReturnsFalseWhenNoElementsMatchThePredicate() {
		expectPredicateExecution(predicated.get(0), false);
		expectPredicateExecution(predicated.get(1), false);

		assertFalse(exists(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	public void testExistsReturnsTrueWhenAtLeastOneElementMatchesThePredicate() {
		expectPredicateExecution(predicated.get(0), true);

		assertTrue(exists(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	public void testAllReturnsTrueWithEmptyCollection() {
		assertTrue(all(Coercions.set(), predicate));
	}

	@SuppressWarnings("unchecked")
	public void testAllReturnsFalseWhenAtLeastOneElementDoesNotMatchThePredicate() {
		expectPredicateExecution(predicated.get(0), false);

		assertFalse(all(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	public void testZeroOrOneReturnsTrueWithEmptyCollection() {
		assertTrue(zeroOrOne(Coercions.set(), predicate));
	}

	@SuppressWarnings("unchecked")
	public void testZeroOrOneReturnsTrueWhenNoElementsMatchThePredicate() {
		expectPredicateExecution(predicated.get(0), false);
		expectPredicateExecution(predicated.get(1), false);

		assertTrue(zeroOrOne(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	public void testZeroOrOneReturnsTrueWhenOneElementMatchesThePredicate() {
		expectPredicateExecution(predicated.get(0), true);
		expectPredicateExecution(predicated.get(1), false);

		assertTrue(zeroOrOne(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	public void testZeroOrOneReturnsFalseWhenMoreThanOneElementMatchesThePredicate() {
		expectPredicateExecution(predicated.get(0), true);
		expectPredicateExecution(predicated.get(1), true);

		assertFalse(zeroOrOne(predicated, predicate));
	}

	public void testInvertCreatesAnInvertingFilter() {
		assertInvertedFilterResult(false);
		assertInvertedFilterResult(true);
	}

	@SuppressWarnings("unchecked")
	private void assertInvertedFilterResult(boolean filterReturnValue) {
		Object testObject = new Object();
		Filter inverted = invert(createPredicateWithExpectation(testObject, filterReturnValue));
		assertEquals(Boolean.valueOf(!filterReturnValue), inverted.execute(testObject));
	}

	@SuppressWarnings("unchecked")
    public void testAndCreatesAFilterThatAlwaysReturnsTrueIfTheGivenListOfFiltersIsEmpty() {
		assertTrue(FirstOrderLogic.and().execute(new Object()));
	}

	@SuppressWarnings("unchecked")
	public void testAndCreatesAShortCircuitingConjunctiveFilter() {
		Object testObject = new Object();
		assertEquals(Boolean.FALSE, FirstOrderLogic.and(createPredicateWithExpectation(testObject, false), (Filter) mock(Filter.class).proxy()).execute(testObject));
		assertEquals(Boolean.FALSE, FirstOrderLogic.and(createPredicateWithExpectation(testObject, true), createPredicateWithExpectation(testObject, false)).execute(testObject));
		assertEquals(Boolean.TRUE, FirstOrderLogic.and(createPredicateWithExpectation(testObject, true), createPredicateWithExpectation(testObject, true)).execute(testObject));
	}

	@SuppressWarnings("unchecked")
    public void testOrCreatesAFilterThatAlwaysReturnsFalseIfTheGivenListOfFiltersIsEmpty() {
		assertFalse(FirstOrderLogic.or().execute(new Object()));
	}

	@SuppressWarnings("unchecked")
	public void testOrCreatesAShortCircuitingDisjunctiveFilter() {
		Object testObject = new Object();
		assertEquals(Boolean.FALSE, FirstOrderLogic.or(createPredicateWithExpectation(testObject, false), createPredicateWithExpectation(testObject, false)).execute(testObject));
		assertEquals(Boolean.TRUE, FirstOrderLogic.or(createPredicateWithExpectation(testObject, true), (Filter) mock(Filter.class).proxy()).execute(testObject));
		assertEquals(Boolean.TRUE, FirstOrderLogic.or(createPredicateWithExpectation(testObject, false), createPredicateWithExpectation(testObject, true)).execute(testObject));
	}

	@SuppressWarnings("unchecked")
    public void testIntersectionReturnsEmptySetWhenNoArgumentsAreGiven() {
        assertTrue(intersection().isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testIntersectionReturnsAllElementsIfOnlyOneArgumentIsGiven() {
        assertEquals(Coercions.set(1, 2, 3), intersection(Coercions.set(1, 2, 3)));
    }

    @SuppressWarnings("unchecked")
    public void testIntersectionReturnsIntersectionIfSeveralArgumentsAreGiven() {
        assertEquals(Coercions.set(1, 2), intersection(Coercions.set(1, 2, 3), Coercions.set(1, 2, 4)));
    }

    @SuppressWarnings("unchecked")
    public void testIntersectionReturnsEmptySetIfIntersectionIsEmpty() {
        assertEquals(Coercions.set(), intersection(Coercions.set(1, 2), Coercions.set(3, 4, 5)));
    }
    
    private void expectPredicateExecution(Object value, boolean returnValue) {
		expectPredicateExecution(mockPredicate, value, returnValue);
	}

	@SuppressWarnings("unchecked")
    private Filter createPredicateWithExpectation(Object testObject, boolean filterReturnValue) {
		Mock mockPredicate = mock(Filter.class);
		expectPredicateExecution(mockPredicate, testObject, filterReturnValue);
		return (Filter) mockPredicate.proxy();
	}

	private void expectPredicateExecution(Mock mockPredicate, Object testObject, boolean filterReturnValue) {
		mockPredicate.expects(once()).method("execute").with(same(testObject)).will(returnValue(filterReturnValue));
	}
}
