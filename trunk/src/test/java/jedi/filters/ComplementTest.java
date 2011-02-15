package jedi.filters;

import static java.util.Arrays.asList;
import static jedi.filters.Complement.minus;
import static jedi.functional.FunctionalPrimitives.select;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class ComplementTest {

	private final List<String> A = asList("a", "b", "c", "d", "x");
	private final List<String> B = asList("a", "b", "c", "d", "e", "f");

	@Test
	public void exampleUsage() {
		assertThat(select(B, minus(A)), is(asList("e", "f")));
	}

	@Test
	public void anotherExampleUsage() {
		assertThat(select(A, minus(B)), is(asList("x")));
	}

	@Test
	public void shouldReturnSetBComplement() {
		assertThat(Complement.minus(A).execute("a"), is(false));
		assertThat(Complement.minus(A).execute("f"), is(true));
		assertThat(Complement.minus(A).execute("x"), is(false));
		assertThat(Complement.minus(A).execute("y"), is(true));
	}

	@Test
	public void shouldToString() {
		assertThat(minus(B).toString(), is("minus [a, b, c, d, e, f]"));
	}

	@Test(expected = jedi.assertion.AssertionError.class)
	public void shouldNotAllowNull() {
		Complement.minus(null);
	}

}