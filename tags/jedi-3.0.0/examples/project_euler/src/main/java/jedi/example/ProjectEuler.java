package jedi.example;

import jedi.annotation.JediCut;
import jedi.annotation.JediFilter;
import jedi.annotation.JediFunctor;

import java.util.ArrayList;
import java.util.List;

import static jedi.assertion.Assert.assertEqual;
import static jedi.assertion.Assert.assertTrue;
import static jedi.example.ProjectEulerStaticClosureFactory.*;
import static jedi.functional.Coercions.list;
import static jedi.functional.Comparables.sort;
import static jedi.functional.FirstOrderLogic.or;
import static jedi.functional.FunctionalPrimitives.*;

public class ProjectEuler {
	
	/**
	 * Find the sum of all the multiples of 3 or 5 below 1000.
	 */
	@SuppressWarnings("unchecked")
	public void problemOne() {
		assertEqual(new Integer(233168), 				//
				fold(0, 								//
					select(								//
						range(1,1000), 					//
						or(								//
							divProxyFilter(this, 3),			//
							divProxyFilter(this, 5))),		//
						sumProxyFunctor2(this)),				//
					"Problem one: Find the sum of all the multiples of 3 or 5 below 1000.");
	}
	
	/**
	 * The sum of the even Fibonacci numbers which are at most 4,000,000
	 */
	public void problemTwo() {
		assertEqual(new Integer(4613732), 		//
				reduce(
                        select(                        //
                                fib(4000000),             //
                                divProxyFilter(this, 2)),     //
                        sumProxyFunctor2(this)),			//
					"Problem two: The sum of the even Fibonacci numbers which are at most 4,000,000");
	}
	
	/**
	 * What is the largest prime factor of the number 600851475143 ?
	 */
	public void problemThree() {
		assertTrue(true, "Problem three: Nothing interesting for JEDI to do!");
	}
	
	/**
	 * Find the largest palindrome made from the product of two 3-digit numbers.
	 */
	public void problemFour() {
		assertEqual(new Integer(906609),								//
				head(													//
					reverse(											//
						sort(											//
							select(										//
								produce(								//
									range(100,1000), 					//
									range(100,1000), 					//
									multiplyProxyFunctor2(this)), 			//
								isPalindromeProxyFilter(this))))),			//
					"Problem four: Find the largest palindrome made from the product of two 3-digit numbers.");
	}
	
	@JediFilter
	boolean isPalindrome(Integer x) {
		String s = x.toString();
		return s.equals(new StringBuffer(s).reverse().toString());
	}
	
	/**
	 * @return x % y == 0
	 */
	@JediFilter(cut= {@JediCut(parameters="y")})
	boolean div(Integer x, Integer y) {
		return x % y == 0;
	}
	
	@JediFunctor
	Integer sum(Integer a, Integer b) {
		return a + b;
	}

	@JediFunctor
	Integer multiply(Integer a, Integer b) {
		return a * b;
	}
	
	public List<Integer> fib(int limit) {
		return fibh(new ArrayList<Integer>(), limit);
	}
	
	private List<Integer> fibh(List<Integer> c, int limit) {
		if (c.isEmpty()) return fibh(list(new Integer(1), new Integer(0)), limit);
		final int sum = c.get(0) + c.get(1);
		if (sum > limit) return c;
		c.add(0, sum);
		return fibh(c, limit);
	}
	
	public List<Integer> range(int a, int b) {
		List<Integer> list = new ArrayList<Integer>(b - a);
		for (int i = a; i < b; i++) {
			list.add(i);
		}
		return list;
	}
	
	public static void main(String[] args) {
		ProjectEuler projectEuler = new ProjectEuler();
		projectEuler.problemOne();
		projectEuler.problemTwo();
		projectEuler.problemThree();
		projectEuler.problemFour();
	}
	
}
