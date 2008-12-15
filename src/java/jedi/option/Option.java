package jedi.option;

import java.util.List;

import jedi.functional.Command;
import jedi.functional.Command0;
import jedi.functional.Filter;
import jedi.functional.Functor;
import jedi.functional.Functor0;

/**
 * An optional value inspired by sensible languages like <a
 * href="http://www.scala-lang.org/docu/files/api/scala/Option.html">Scala</a>.
 */
public interface Option<T> extends Iterable<T> {

	/**
	 * Accept a for this {@link Option}.
	 * In languages like Scala we can do this kind of thing:
	 * <pre>
	 * x match {
	 *   case {@link Some}(s) =&gt; s
	 *   case {@link None} =&gt; &quot;?&quot;
	 * }
	 * </pre>
	 * 
	 * In Java, such elegance is not possible so this method is actually a
	 * Visitor implementation:
	 * 
	 * <pre>
	 * x.match(new OptionMatcher() {
	 *   public void caseSome(T value) { // excellent we can use the value }
	 *   public void caseNone() { // no value, deal with it }
	 * });
	 * </pre>
	 */
	void match(OptionMatcher<? super T> matcher);

	/**
	 * A match strategy based on Commands. Since annotated methods become
	 * commands in Jedi, this approach is a very simple solution to dealing with
	 * {@link Some} and {@link None}.
	 * 
	 * @param someCommand
	 *            a command that will receive the callback in the event that
	 *            {@link Some} is matched
	 * @param noneCommand
	 *            a command that will receive the callback in the event that
	 *            {@link None} is matched
	 */
	void match(Command<? super T> someCommand, Command0 noneCommand);

	/**
	 * A match strategy based on Functors.
	 * 
	 * @param someFunctor
	 *            the functor to execute if this Option is a Same
	 * @param noneFunctor
	 *            the functor to execute if this Option is a {@link None}
	 * @return the result of executing the functor
	 */
	<R1, R2 extends R1> R1 match(Functor<? super T, R1> someFunctor, Functor0<R2> noneFunctor);

	/**
	 * An empty list for {@link None} or an immutable list with
	 * {@link Some#get}.
	 */
	List<T> asList();

	/**
	 * If the option is {@link Some} return its value, otherwise return the
	 * result of evaluating the generator expression.
	 * 
	 * @param generator
	 *            the default expression.
	 */
	T getOrElse(Functor0<? extends T> generator);

	/**
	 * If the option is {@link Some} return its value, otherwise return a
	 * default value.
	 */
	T getOrElse(T defaultValue);

	/**
	 * If the option is {@link Some}, return a function applied to its
	 * value, wrapped in a {@link Some}: <code>{@link Some}(f(this.get))</code>, otherwise
	 * return {@link None}.
	 * 
	 * @param mappingFunction
	 *            the function to apply
	 */
	<R> Option<R> map(Functor<? super T, R> mappingFunction);

	/**
	 * Apply the given Command to the option's value if it is not {@link None}. Do
	 * nothing if it is {@link None}.
	 * 
	 * @param command
	 *            the Command to apply.
	 */
	void forEach(Command<? super T> command);

	/**
	 * If this option is {@link Some} and the given functor <code>f</code>
	 * yields <code>false</code> on its value, return {@link None}.
	 * Otherwise return this option.
	 * 
	 * @param f
	 *            the filter used for testing.
	 */
	Option<T> filter(Filter<? super T> f);

	/**
	 * Get the value returning <code>null</code> for None.
	 */
	T unsafeGet();

}
