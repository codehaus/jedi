package jedi.functional;

public interface Command<T> {
	void execute(T value);
}
