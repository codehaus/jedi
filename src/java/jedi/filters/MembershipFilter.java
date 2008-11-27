package jedi.filters;

import static jedi.functional.Coercions.*;

import java.util.Collection;
import java.util.Set;

public class MembershipFilter<T> extends AbstractUnaryFilter<T, Set<T>> {
	public MembershipFilter(Set<T> collection) {
		super(collection);
	}

	public static <T> MembershipFilter<T> create(Collection<T> collection) {
		return new MembershipFilter<T>(asSet(collection));
	}

	public Boolean execute(T value) {
		return getTestValue().contains(value);
	}

	@Override
	protected String getFunctionName() {
		return "member";
	}
}
