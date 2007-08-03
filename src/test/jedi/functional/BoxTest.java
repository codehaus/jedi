package jedi.functional;

import static jedi.functional.Coercions.array;
import static jedi.functional.Coercions.list;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

public class BoxTest extends TestCase {

	public void testBoxAndUnboxWithBooleans() {
		boolean[] b = new boolean[] { true, false, true };
		Boolean[] box = Box.box(b);
		assertArraysEqual(array(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), box);
		assertTrue(Arrays.equals(b, Box.unbox(box)));
	}

	public void testBoxAndUnboxWithBytes() {
		byte[] b = new byte[] { 1, 2, 3 };
		Byte[] box = Box.box(b);
		assertArraysEqual(array(new Byte((byte) 1), new Byte((byte) 2), new Byte((byte) 3)), box);
		assertTrue(Arrays.equals(b, Box.unbox(box)));
	}

	public void testBoxAndUnboxWithChars() {
		char[] b = new char[] { 'a', 'b', 'c' };
		Character[] box = Box.box(b);
		assertArraysEqual(array('a', 'b', 'c'), box);
		assertTrue(Arrays.equals(b, Box.unbox(box)));
	}

	public void testBoxAndUnboxWithDoubles() {
		double[] b = new double[] { 1, 2, 3 };
		Double[] box = Box.box(b);
		assertArraysEqual(array(1d, 2d, 3d), box);
		assertTrue(Arrays.equals(b, Box.unbox(box)));
	}

	public void testBoxAndUnboxWithFloats() {
		float[] b = new float[] { 1, 2, 3 };
		Float[] box = Box.box(b);
		assertArraysEqual(array(1f, 2f, 3f), box);
		assertTrue(Arrays.equals(b, Box.unbox(box)));
	}

	public void testBoxAndUnboxWithInts() {
		int[] b = new int[] { 1, 2, 3 };
		Integer[] box = Box.box(b);
		assertArraysEqual(array(1, 2, 3), box);
		assertTrue(Arrays.equals(b, Box.unbox(box)));
	}

	public void testBoxAndUnboxWithLongs() {
		long[] b = new long[] { 1, 2, 3 };
		Long[] box = Box.box(b);
		assertArraysEqual(array(1L, 2L, 3L), box);
		assertTrue(Arrays.equals(b, Box.unbox(box)));
	}

	public void testBoxAndUnboxWithShorts() {
		short[] b = new short[] { 1, 2, 3 };
		Short[] box = Box.box(b);
		assertArraysEqual(array((short) 1, (short) 2, (short) 3), box);
		assertTrue(Arrays.equals(b, Box.unbox(box)));
	}

	public void testBoxDoubles() {
		assertArraysEqual(array(1d, 2d, 3d), Box.boxDoubles(1, 2, 3));
	}

	public void testBoxFloats() {
		assertArraysEqual(array(1f, 2f, 3f), Box.boxFloats(1, 2, 3));
	}

	public void testBoxInts() {
		assertArraysEqual(array(1, 2, 3), Box.boxInts(1, 2, 3));
	}

	public void testBoxLongs() {
		assertArraysEqual(array(1L, 2L, 3L), Box.boxLongs(1, 2, 3));
	}

	public void testBoxShorts() {
		assertArraysEqual(array((short) 1, (short) 2, (short) 3), Box.boxShorts((short) 1, (short) 2, (short) 3));
	}

	public void testUnBoxBooleanCollection() throws Exception {
		Collection<Boolean> collection = list(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
		assertTrue(Arrays.equals(new boolean[] { true, false, true }, Box.unbox(collection)));
	}

	public void testUnBoxByteCollection() throws Exception {
		Collection<Byte> collection = list(new Byte((byte) 1), new Byte((byte) 2), new Byte((byte) 3));
		assertTrue(Arrays.equals(new byte[] { 1, 2, 3 }, Box.unbox(collection)));
	}

	public void testUnBoxCharacterCollection() throws Exception {
		Collection<Character> collection = list('a', 'b', 'c');
		assertTrue(Arrays.equals(new char[] { 'a', 'b', 'c' }, Box.unbox(collection)));
	}

	public void testUnBoxShortCollection() throws Exception {
		Collection<Short> collection = Coercions.asList(array((short) 1, (short) 2, (short) 3));
		assertTrue(Arrays.equals(new short[] { (short) 1, (short) 2, (short) 3 }, Box.unbox(collection)));
	}

	public void testUnBoxIntegerCollection() throws Exception {
		Collection<Integer> collection = list(1, 2, 3);
		assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, Box.unbox(collection)));
	}

	public void testUnBoxLongCollection() throws Exception {
		Collection<Long> collection = list(1L, 2L, 3L);
		assertTrue(Arrays.equals(new long[] { 1, 2, 3 }, Box.unbox(collection)));
	}

	public void testUnBoxFloatCollection() throws Exception {
		Collection<Float> collection = list(1F, 2F, 3F);
		assertTrue(Arrays.equals(new float[] { 1, 2, 3 }, Box.unbox(collection)));
	}

	public void testUnBoxDoubleCollection() throws Exception {
		Collection<Double> collection = list(1D, 2D, 3D);
		assertTrue(Arrays.equals(new double[] { 1, 2, 3 }, Box.unbox(collection)));
	}

	private void assertArraysEqual(Object[] a, Object[] b) {
		assertTrue(Arrays.equals(a, b));
	}

}
