package jedi.functional;

import static jedi.assertion.Assert.assertNotNull;
import static jedi.assertion.Assert.assertTrue;
import static jedi.functional.FirstOrderLogic.all;

import java.util.Collection;

import jedi.filters.NotNullFilter;

/**
 * Facilities for boxing and unboxing arrays and also unboxing the contents of
 * collections into arrays.
 */
public class Box {

	private static final NotNullFilter<Object> NOT_NULL_FILTER = new NotNullFilter<Object>();

	public static Boolean[] box(boolean[] unboxed) {
		assertNotNull(unboxed, "unboxed must not be null");

		Boolean[] boxed = new Boolean[unboxed.length];
		for (int i = 0; i < unboxed.length; i++) {
			boxed[i] = unboxed[i];
		}

		return boxed;
	}

	public static boolean[] unbox(Boolean[] boxed) {
		assertNothingNull(boxed);

		boolean[] unboxed = new boolean[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}

		return unboxed;
	}

	public static boolean[] unbox(Collection<Boolean> boxed) {
		assertNotNull(boxed, "boxed must not be null");
		return unbox(Coercions.asArray(boxed));
	}

	public static Byte[] box(byte[] unboxed) {
		assertNotNull(unboxed, "unboxed must not be null");

		Byte[] boxed = new Byte[unboxed.length];
		for (int i = 0; i < unboxed.length; i++) {
			boxed[i] = unboxed[i];
		}

		return boxed;
	}

	public static byte[] unbox(Byte[] boxed) {
		assertNothingNull(boxed);

		byte[] unboxed = new byte[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}

		return unboxed;
	}

	public static byte[] unbox(Collection<Byte> boxed) {
		assertNotNull(boxed, "boxed must not be null");
		return unbox(Coercions.asArray(boxed));
	}

	public static Character[] box(char[] unboxed) {
		assertNotNull(unboxed, "unboxed must not be null");

		Character[] boxed = new Character[unboxed.length];
		for (int i = 0; i < unboxed.length; i++) {
			boxed[i] = unboxed[i];
		}

		return boxed;
	}

	public static char[] unbox(Character[] boxed) {
		assertNothingNull(boxed);

		char[] unboxed = new char[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}

		return unboxed;
	}

	public static char[] unbox(Collection<Character> boxed) {
		assertNotNull(boxed, "boxed must not be null");
		return unbox(Coercions.asArray(boxed));
	}

	public static Short[] boxShorts(short... unboxed) {
		assertNotNull(unboxed, "unboxed must not be null");

		Short[] boxed = new Short[unboxed.length];
		for (int i = 0; i < unboxed.length; i++) {
			boxed[i] = unboxed[i];
		}

		return boxed;
	}

	public static Short[] box(short[] unboxed) {
		return boxShorts(unboxed);
	}

	public static short[] unbox(Short[] boxed) {
		assertNothingNull(boxed);

		short[] unboxed = new short[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}

		return unboxed;
	}

	public static short[] unbox(Collection<Short> boxed) {
		assertNotNull(boxed, "boxed must not be null");
		return unbox(Coercions.asArray(boxed));
	}


	public static Integer[] boxInts(int... unboxed) {
		assertNotNull(unboxed, "unboxed must not be null");

		Integer[] boxed = new Integer[unboxed.length];
		for (int i = 0; i < unboxed.length; i++) {
			boxed[i] = unboxed[i];
		}

		return boxed;
	}

	public static Integer[] box(int[] unboxed) {
		return boxInts(unboxed);
	}

	public static int[] unbox(Integer[] boxed) {
		assertNothingNull(boxed);

		int[] unboxed = new int[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}

		return unboxed;
	}

	public static int[] unbox(Collection<Integer> boxed) {
		assertNotNull(boxed, "boxed must not be null");
		return unbox(Coercions.asArray(boxed));
	}

	public static Long[] box(long[] unboxed) {
		return boxLongs(unboxed);
	}

	public static Long[] boxLongs(long... unboxed) {
		assertNotNull(unboxed, "unboxed must not be null");

		Long[] boxed = new Long[unboxed.length];
		for (int i = 0; i < unboxed.length; i++) {
			boxed[i] = unboxed[i];
		}

		return boxed;
	}

	public static long[] unbox(Long[] boxed) {
		assertNothingNull(boxed);

		long[] unboxed = new long[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}

		return unboxed;
	}

	public static long[] unbox(Collection<Long> boxed) {
		assertNotNull(boxed, "boxed must not be null");
		return unbox(Coercions.asArray(boxed));
	}

	public static Float[] box(float[] unboxed) {
		return boxFloats(unboxed);
	}

	public static Float[] boxFloats(float... unboxed) {
		assertNotNull(unboxed, "unboxed must not be null");

		Float[] boxed = new Float[unboxed.length];
		for (int i = 0; i < unboxed.length; i++) {
			boxed[i] = unboxed[i];
		}

		return boxed;
	}

	public static float[] unbox(Float[] boxed) {
		assertNothingNull(boxed);

		float[] unboxed = new float[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}

		return unboxed;
	}

	public static float[] unbox(Collection<Float> boxed) {
		assertNotNull(boxed, "boxed must not be null");
		return unbox(Coercions.asArray(boxed));
	}

	public static Double[] boxDoubles(double... unboxed) {
		assertNotNull(unboxed, "unboxed must not be null");

		Double[] boxed = new Double[unboxed.length];
		for (int i = 0; i < unboxed.length; i++) {
			boxed[i] = unboxed[i];
		}

		return boxed;
	}

	public static Double[] box(double[] unboxed) {
		return boxDoubles(unboxed);
	}

	public static double[] unbox(Double[] boxed) {
		assertNothingNull(boxed);

		double[] unboxed = new double[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}

		return unboxed;
	}

	public static double[] unbox(Collection<Double> boxed) {
		assertNotNull(boxed, "boxed must not be null");
		return unbox(Coercions.asArray(boxed));
	}

	private static void assertNothingNull(Object[] boxed) {
		assertNotNull(boxed, "boxed must not be null");
		assertTrue(all(Coercions.asList(boxed), NOT_NULL_FILTER), "boxed must not contain any null elements");
	}

	private Box() {
	}
}
