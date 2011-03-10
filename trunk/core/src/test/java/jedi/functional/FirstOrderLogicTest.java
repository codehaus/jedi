package jedi.functional;

import static jedi.functional.Coercions.set;
import static jedi.functional.FirstOrderLogic.all;
import static jedi.functional.FirstOrderLogic.exists;
import static jedi.functional.FirstOrderLogic.intersection;
import static jedi.functional.FirstOrderLogic.invert;
import static jedi.functional.FirstOrderLogic.union;
import static jedi.functional.FirstOrderLogic.xor;
import static jedi.functional.FirstOrderLogic.zeroOrOne;

import java.util.HashSet;
import java.util.List;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.jmock.core.InvocationMatcher;
import org.junit.Test;

public class FirstOrderLogicTest extends MockObjectTestCase {

	private final Mock mockPredicate = mock(Filter.class);
	@SuppressWarnings("unchecked")
	private final Filter predicate = (Filter) mockPredicate.proxy();
	private final List<?> predicated = Coercions.list(new Object(), new Object());

	@SuppressWarnings("unchecked")
	@Test
	public void testExistsReturnsFalseWithEmptyCollection() {
		assertFalse(exists(set(), predicate));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExistsReturnsFalseWhenNoElementsMatchThePredicate() {
		expectPredicateExecution(predicated.get(0), false);
		expectPredicateExecution(predicated.get(1), false);

		assertFalse(exists(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExistsReturnsTrueWhenAtLeastOneElementMatchesThePredicate() {
		expectPredicateExecution(predicated.get(0), true);

		assertTrue(exists(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAllReturnsTrueWithEmptyCollection() {
		assertTrue(all(set(), predicate));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAllReturnsFalseWhenAtLeastOneElementDoesNotMatchThePredicate() {
		expectPredicateExecution(predicated.get(0), false);

		assertFalse(all(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testZeroOrOneReturnsTrueWithEmptyCollection() {
		assertTrue(zeroOrOne(set(), predicate));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testZeroOrOneReturnsTrueWhenNoElementsMatchThePredicate() {
		expectPredicateExecution(predicated.get(0), false);
		expectPredicateExecution(predicated.get(1), false);

		assertTrue(zeroOrOne(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testZeroOrOneReturnsTrueWhenOneElementMatchesThePredicate() {
		expectPredicateExecution(predicated.get(0), true);
		expectPredicateExecution(predicated.get(1), false);

		assertTrue(zeroOrOne(predicated, predicate));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testZeroOrOneReturnsFalseWhenMoreThanOneElementMatchesThePredicate() {
		expectPredicateExecution(predicated.get(0), true);
		expectPredicateExecution(predicated.get(1), true);

		assertFalse(zeroOrOne(predicated, predicate));
	}

	@Test
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
	@Test
	public void testAndCreatesAFilterThatAlwaysReturnsTrueIfTheGivenListOfFiltersIsEmpty() {
		assertTrue(FirstOrderLogic.and().execute(new Object()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAndCreatesAShortCircuitingConjunctiveFilter() {
		Object testObject = new Object();
		assertEquals(Boolean.FALSE, FirstOrderLogic.and(createPredicateWithExpectation(testObject, false),
				(Filter) mock(Filter.class).proxy()).execute(testObject));
		assertEquals(Boolean.FALSE, FirstOrderLogic.and(createPredicateWithExpectation(testObject, true),
				createPredicateWithExpectation(testObject, false)).execute(testObject));
		assertEquals(Boolean.TRUE, FirstOrderLogic.and(createPredicateWithExpectation(testObject, true),
				createPredicateWithExpectation(testObject, true)).execute(testObject));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOrCreatesAFilterThatAlwaysReturnsFalseIfTheGivenListOfFiltersIsEmpty() {
		assertFalse(FirstOrderLogic.or().execute(new Object()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOrCreatesAShortCircuitingDisjunctiveFilter() {
		Object testObject = new Object();
		assertEquals(Boolean.FALSE, FirstOrderLogic.or(createPredicateWithExpectation(testObject, false),
				createPredicateWithExpectation(testObject, false)).execute(testObject));
		assertEquals(Boolean.TRUE, FirstOrderLogic
				.or(createPredicateWithExpectation(testObject, true), (Filter) mock(Filter.class).proxy()).execute(testObject));
		assertEquals(Boolean.TRUE, FirstOrderLogic.or(createPredicateWithExpectation(testObject, false),
				createPredicateWithExpectation(testObject, true)).execute(testObject));
	}

	@Test
	public void testXor() {
		Object testObject = new Object();
		assertEquals(Boolean.FALSE, xor(filter(false),filter(false)).execute(testObject));
		assertEquals(Boolean.TRUE, xor(filter(false),filter(true)).execute(testObject));
		assertEquals(Boolean.TRUE, xor(filter(true),filter(false)).execute(testObject));
		assertEquals(Boolean.FALSE, xor(filter(true),filter(true)).execute(testObject));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIntersectionReturnsEmptySetWhenNoArgumentsAreGiven() {
		assertTrue(intersection().isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIntersectionReturnsAllElementsIfOnlyOneArgumentIsGiven() {
		assertEquals(set(1, 2, 3), intersection(set(1, 2, 3)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIntersectionReturnsIntersectionIfSeveralArgumentsAreGiven() {
		assertEquals(set(1, 2), intersection(set(1, 2, 3), set(1, 2, 4)));
	}

	@Test
	public void testIntersectionReturnsEmptySetIfIntersectionIsEmpty() {
		assertEquals(set(), intersection(set(1, 2), set(3, 4, 5)));
	}

	@Test
	public void testUnionReturnsAllElementsIfOnlyOneArgumentIsGiven() {
		assertEquals(set(1, 2, 3), union(set(1, 2, 3)));
	}

	@Test
	public void testUnionReturnsUnionIfSeveralArgumentsAreGiven() {
		assertEquals(set(1, 2, 3, 4), union(set(1, 2, 3), set(1, 2, 4)));
	}

    @Test
	public void testUnionReturnsEmptySetForEmptyIterable() {
		assertEquals(set(), union(new HashSet()));
	}

	private void expectPredicateExecution(Object value, boolean returnValue) {
		expectPredicateExecution(mockPredicate, value, returnValue, once());
	}

	@SuppressWarnings("unchecked")
	private Filter createPredicateWithExpectation(Object testObject, boolean filterReturnValue) {
		return createPredicateWithExpectation(testObject, filterReturnValue, once());
	}

	private Filter createPredicateWithExpectation(Object testObject, boolean filterReturnValue, InvocationMatcher matcher) {
		Mock mockPredicate = mock(Filter.class);
		expectPredicateExecution(mockPredicate, testObject, filterReturnValue, matcher);
		return (Filter) mockPredicate.proxy();
	}

	private void expectPredicateExecution(Mock mockPredicate, Object testObject, boolean filterReturnValue, InvocationMatcher matcher) {
		mockPredicate.expects(matcher).method("execute").with(same(testObject)).will(returnValue(filterReturnValue));
	}

	private Filter filter(final boolean filterReturnValue) {
		return new Filter<Object>() {
			public Boolean execute(Object value) {
				return filterReturnValue;
			}
		};
	}
}
