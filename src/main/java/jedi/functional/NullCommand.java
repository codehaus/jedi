package jedi.functional;

/**
 * A Command that does nothing.
 * @author channing
 *
 * @param <T>
 */
public final class NullCommand<T> implements Command<T> {

	/**
	 * Do nothing with the value
	 */
	public void execute(T value) {
		// do nothing
	}

}
