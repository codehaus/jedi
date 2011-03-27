package jedi.functional;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class EmptyIterator<T> implements Iterator<T>, Serializable {
	public boolean hasNext() {
		return false;
	}

	public T next() {
		throw new NoSuchElementException();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}