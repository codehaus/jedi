package jedi.option;

import java.util.List;

import jedi.functional.Command;
import jedi.functional.Filter;
import jedi.functional.Functor;
import jedi.functional.Generator;

/**
 * An optional value inspired by sensible languages like <a
 * href="http://www.scala-lang.org/docu/files/api/scala/Option.html">Scala</a>.
 */
public interface Option<T> {

	/**
	 * Emulates Scala's pattern match on optional types.
	 * 
	 * <pre>
	 * x match { 
	 *   case Some(s) =&gt; s 
	 *   case None =&gt; &quot;?&quot; 
	 * }
	 * </pre>
	 * 
	 * In Java, such elegance is not possible so this method is actually a
	 * Visitor implementation.
	 * 
	 * @param matcher
	 */
	void match(OptionMatcher<T> matcher);

	/**
	 * A match strategy based on Commands. Since annotated methods become
	 * commands in Jedi, this approach is a very simple solution to dealing with
	 * Some and None.
	 * 
	 * @param someCommand
	 *            a command executing against type T
	 * @param noneCommand
	 *            a command executing against None
	 */
	void match(Command<T> someCommand, Command<None<T>> noneCommand);

	/**
	 * @return an empty list or an immutable list with Some.get
	 */
	List<T> asList();

	/**
	 * If the option is nonempty return its value, otherwise return the result
	 * of evaluating the generator expression. <br/>
	 * This is useful if there are advantages to computing the default value
	 * lazily
	 * 
	 * @param generator
	 *            the default expression.
	 */
	T getOrElse(Generator<T> generator);

	/**
	 * If the option is nonempty return its value, otherwise return a default
	 * value.
	 */
	T getOrElse(T defaultValue);

	/**
	 * If the option is nonempty, return a function applied to its value,
	 * wrapped in a Some i.e. <code>Some(f(this.get))</code>. Otherwise return
	 * <code>None</code>.
	 * 
	 * @param f
	 *            the function to apply
	 */
	<R> Option<R> map(Functor<T, R> mappingFunction);

	/**
	 * Apply the given Command to the option's value if it is not None. Do
	 * nothing if it is None.
	 * 
	 * @param command
	 *            the Command to apply.
	 */
	void forEach(Command<T> command);

	/**
	 * If this option is {@link Some} and the given functor <code>f</code> yields
	 * <code>false</code> on its value, return <code>None</code>. Otherwise
	 * return this option.
	 * 
	 * @param f
	 *            the filter used for testing.
	 */
	Option<T> filter(Filter<T> f);
}
