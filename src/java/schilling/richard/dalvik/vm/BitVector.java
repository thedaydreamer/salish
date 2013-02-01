package schilling.richard.dalvik.vm;

import java.util.BitSet;

/**
 * Expanding bitmap, used for tracking resources. Bits are numbered starting
 * from zero.
 * 
 * <p>
 * All operations on a BitVector are unsynchronized.
 * 
 * <p>
 * Although this class does not add any functionality to BitSet at all, it's
 * created to maintain name compatability with the original C code.
 * 
 */
public class BitVector extends BitSet implements Comparable<BitVector> {
	/*
	 * Determine whether or not the specified bit is set. THis just calls
	 * pBits.get(num), but this function is kept to retain name compatability
	 * with the c library.
	 */
	public boolean dvmIsBitSet(BitVector pBits, int num) {
		return pBits.get(num);
	}

	public void copyFrom(BitVector src) {
		clear();
		if (src == null)
			return;

		for (int i = 0; i < src.length(); i++) {
			this.set(i, src.get(i));
		}

	}

	/**
	 * Merge the contents of "src" into "dst", checking to see if this causes
	 * any changes to occur. This is a logical OR.
	 * 
	 * @return Returns "true" if the contents of the destination vector were
	 *         modified.
	 */
	public static boolean dvmCheckMergeBitVectors(BitVector dst, BitVector src) {
		boolean changed = false;

		checkSizes(dst, src);

		int idx;
		for (idx = 0; idx < dst.size(); idx++) {
			boolean dstValue = dst.get(idx);
			boolean srcValue = dst.get(idx);
			boolean merged = dstValue || srcValue ? true : false;

			if (dstValue != merged) {
				if (merged)
					dst.set(idx);
				else
					dst.clear(idx);
			}
		}

		return changed;
	}

	/*
	 * If the vector sizes don't match, log an error and abort.
	 */
	public static void checkSizes(BitVector bv1, BitVector bv2) {
		if (bv1.size() != bv2.size()) {
			Init.dvmAbort();
			throw new RuntimeException("bit vectors have different sizes.");

		}
	}

	@Override
	public int compareTo(BitVector another) {
		int result = this.length() - another.length();
		if (result != 0)
			return result;

		result = this.cardinality() - another.cardinality();
		if (result != 0)
			return result;

		int len = this.length();
		for (int i = 0; i < len; i++) {
			if (this.get(i) != another.get(i)) {
				return this.get(i) ? 1 : -1;
			}

		}
		return 0;
	}

}
