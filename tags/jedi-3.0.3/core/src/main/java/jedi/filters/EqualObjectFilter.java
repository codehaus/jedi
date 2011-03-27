package jedi.filters;

public class EqualObjectFilter<T> extends AbstractUnaryFilter<T, T> {
	public EqualObjectFilter(T toFind) {
		super(toFind);
	}

	public static <T> EqualObjectFilter<T> create(T toFind) {
		return new EqualObjectFilter<T>(toFind);
	}

	public Boolean execute(T t) {
		return getTestValue().equals(t);
	}

	@Override
	protected String getFunctionName() {
		return "equals";
	}
}
