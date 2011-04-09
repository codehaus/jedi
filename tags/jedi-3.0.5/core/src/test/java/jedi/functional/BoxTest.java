package jedi.functional;

import static jedi.functional.Box.boxBytes;
import static jedi.functional.Box.boxBooleans;
import static jedi.functional.Box.boxChars;
import static jedi.functional.Box.boxDoubles;
import static jedi.functional.Box.boxFloats;
import static jedi.functional.Box.boxInts;
import static jedi.functional.Box.boxLongs;
import static jedi.functional.Box.boxShorts;
import static jedi.functional.Box.unboxBooleans;
import static jedi.functional.Box.unboxBytes;
import static jedi.functional.Box.unboxCharacters;
import static jedi.functional.Box.unboxDoubles;
import static jedi.functional.Box.unboxFloats;
import static jedi.functional.Box.unboxIntegers;
import static jedi.functional.Box.unboxLongs;
import static jedi.functional.Box.unboxShorts;
import static jedi.functional.Coercions.array;
import static jedi.functional.Coercions.list;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

public class BoxTest {

	@Test
	public void testBoxAndUnboxWithBooleans() {
		boolean[] b = new boolean[] { true, false, true };
		Boolean[] box = boxBooleans(b);
		assertArraysEqual(array(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), box);
		assertTrue(Arrays.equals(b, Box.unboxBooleans(box)));
	}

	@Test
	public void testBoxAndUnboxWithBytes() {
		byte[] b = new byte[] { 1, 2, 3 };
		Byte[] box = boxBytes(b);
		assertArraysEqual(array(new Byte((byte) 1), new Byte((byte) 2), new Byte((byte) 3)), box);
		assertTrue(Arrays.equals(b, Box.unboxBytes(box)));
	}

	@Test
	public void testBoxAndUnboxWithChars() {
		char[] b = new char[] { 'a', 'b', 'c' };
		Character[] box = boxChars(b);
		assertArraysEqual(array('a', 'b', 'c'), box);
		assertTrue(Arrays.equals(b, Box.unboxCharacters(box)));
	}

	@Test
	public void testBoxAndUnboxWithDoubles() {
		double[] b = new double[] { 1, 2, 3 };
		Double[] box = boxDoubles(b);
		assertArraysEqual(array(1d, 2d, 3d), box);
		assertTrue(Arrays.equals(b, Box.unboxDoubles(box)));
	}

	@Test
	public void testBoxAndUnboxWithFloats() {
		float[] b = new float[] { 1, 2, 3 };
		Float[] box = boxFloats(b);
		assertArraysEqual(array(1f, 2f, 3f), box);
		assertTrue(Arrays.equals(b, Box.unboxFloats(box)));
	}

	@Test
	public void testBoxAndUnboxWithInts() {
		int[] b = new int[] { 1, 2, 3 };
		Integer[] box = boxInts(b);
		assertArraysEqual(array(1, 2, 3), box);
		assertTrue(Arrays.equals(b, Box.unboxIntegers(box)));
	}

	@Test
	public void testBoxAndUnboxWithLongs() {
		long[] b = new long[] { 1, 2, 3 };
		Long[] box = boxLongs(b);
		assertArraysEqual(array(1L, 2L, 3L), box);
		assertTrue(Arrays.equals(b, Box.unboxLongs(box)));
	}

	@Test
	public void testBoxAndUnboxWithShorts() {
		short[] b = new short[] { 1, 2, 3 };
		Short[] box = boxShorts(b);
		assertArraysEqual(array((short) 1, (short) 2, (short) 3), box);
		assertTrue(Arrays.equals(b, Box.unboxShorts(box)));
	}

	@Test
	public void testBoxDoubles() {
		assertArraysEqual(array(1d, 2d, 3d), boxDoubles(1, 2, 3));
	}

	@Test
	public void testBoxFloats() {
		assertArraysEqual(array(1f, 2f, 3f), boxFloats(1, 2, 3));
	}

	@Test
	public void testBoxInts() {
		assertArraysEqual(array(1, 2, 3), boxInts(1, 2, 3));
	}

	@Test
	public void testBoxLongs() {
		assertArraysEqual(array(1L, 2L, 3L), boxLongs(1, 2, 3));
	}

	@Test
	public void testBoxShorts() {
		assertArraysEqual(array((short) 1, (short) 2, (short) 3), boxShorts((short) 1, (short) 2, (short) 3));
	}

	@Test
	public void testUnboxBooleanCollection() throws Exception {
		Collection<Boolean> collection = list(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
		assertTrue(Arrays.equals(new boolean[] { true, false, true }, unboxBooleans(collection)));
	}

	@Test
	public void testUnboxByteCollection() throws Exception {
		Collection<Byte> collection = list(new Byte((byte) 1), new Byte((byte) 2), new Byte((byte) 3));
		assertTrue(Arrays.equals(new byte[] { 1, 2, 3 }, unboxBytes(collection)));
	}

	@Test
	public void testUnboxCharacterCollection() throws Exception {
		Collection<Character> collection = list('a', 'b', 'c');
		assertTrue(Arrays.equals(new char[] { 'a', 'b', 'c' }, unboxCharacters(collection)));
	}

	@Test
	public void testUnboxShortCollection() throws Exception {
		Collection<Short> collection = Coercions.asList(array((short) 1, (short) 2, (short) 3));
		assertTrue(Arrays.equals(new short[] { (short) 1, (short) 2, (short) 3 }, unboxShorts(collection)));
	}

	@Test
	public void testUnboxIntegerCollection() throws Exception {
		Collection<Integer> collection = list(1, 2, 3);
		assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, unboxIntegers(collection)));
	}

	@Test
	public void testUnboxLongCollection() throws Exception {
		Collection<Long> collection = list(1L, 2L, 3L);
		assertTrue(Arrays.equals(new long[] { 1, 2, 3 }, unboxLongs(collection)));
	}

	@Test
	public void testUnboxFloatCollection() throws Exception {
		Collection<Float> collection = list(1F, 2F, 3F);
		assertTrue(Arrays.equals(new float[] { 1, 2, 3 }, unboxFloats(collection)));
	}

	@Test
	public void testUnboxDoubleCollection() throws Exception {
		Collection<Double> collection = list(1D, 2D, 3D);
		assertTrue(Arrays.equals(new double[] { 1, 2, 3 }, unboxDoubles(collection)));
	}

	private void assertArraysEqual(Object[] a, Object[] b) {
		assertTrue(Arrays.equals(a, b));
	}

}
