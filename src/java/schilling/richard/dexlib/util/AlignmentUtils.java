/*
 * [The "BSD licence"]
 * Copyright (c) 2010 Ben Gruver (JesusFreke)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package schilling.richard.dexlib.util;

/**
 * Utility class to work with blocks and byte alignment.
 * 
 * @author rschilling
 * 
 */
public final class AlignmentUtils {

	private AlignmentUtils() {
	}

	/**
	 * Given the specified offset and block size (alignment), this function
	 * calculates the position in an array where the next block begins.
	 * 
	 * <p>
	 * subtracting one from the result of this function yields the last position
	 * of the block that the offset resides in.
	 * 
	 * @param offset
	 *            the offset to test.
	 * @param alignment
	 *            the size of each segment.
	 * @return the start position of the segment that occurs after offset.
	 */
	public static int alignOffset(int offset, int alignment) {
		if (offset < 0)
			throw new IllegalArgumentException(
					"offset cannot be less than zero.");

		int mask = alignment - 1;
		return (offset + mask) & ~mask;
	}

	/**
	 * Checks to see if an offset position occurs on a segment boundary.
	 * 
	 * @param offset
	 *            the offset to test.
	 * @param alignment
	 *            the size of each segment.
	 * @return true if offset is the first position of a segment boundary false
	 *         otherwise.
	 */
	public static boolean isAligned(int offset, int alignment) {
		return (offset % alignment) == 0;
	}
}
