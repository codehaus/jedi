package jedi.option;

import java.util.Collection;
import java.util.NoSuchElementException;

import jedi.functional.Command;
import jedi.functional.Functor;
import jedi.functional.Generator;

/**
 * An optional value inspired by sensible languages like <a
 * href="http://www.scala-lang.org/docu/files/api/scala/Option.html">Scala</a>.
 */
public interface Option<T> {

	/**
	 * Emulates Scala's pattern match on optional types.
	 * <pre>
	 * x match { 
	 *   case Some(s) => s 
	 *   case None => "?" 
	 * } 
	 * </pre>
	 * In Java, such elegance is not possible so this method is actually a Visitor implementation.
	 * @param matcher
	 */
	void match(OptionMatcher<T> matcher);
	
	/**
	 * A match strategy based on Commands.
	 * Since annotated methods become commands in Jedi, this approach is a very simple solution to
	 * dealing with Some and None.
	 * @param someCommand a command executing against type T
	 * @param noneCommand a command executing against None
	 */
	void match(Command<T> someCommand, Command<None> noneCommand);

	/**
	 * @return <code>true</code> if this Option is a {@link None}
	 */
	boolean isEmpty();

	Collection<T> asList();

	/**
	 * Get the value of this option.
	 * 
	 * @requires that the option is nonEmpty.
	 * @throws NoSuchElementException
	 *             if the option is empty.
	 */
	T get() throws NoSuchElementException;

	/**
	 * If the option is nonempty return its value, otherwise return the result
	 * of evaluating the generator expression.
	 * 
	 * @param generator the default expression.
	 */
	T getOrElse(Generator<T> generator);

	/**
	 * If the option is nonempty, return a function applied to its value,
	 * wrapped in a Some i.e. <code>Some(f(this.get))</code>. Otherwise return
	 * <code>None</code>.
	 * 
	 * @param f
	 *            the function to apply
	 */
	<R> Option<R> map(Functor<T, R> mappingFunction);
}
