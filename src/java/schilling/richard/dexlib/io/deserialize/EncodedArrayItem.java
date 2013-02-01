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

package schilling.richard.dexlib.io.deserialize;

import schilling.richard.dexlib.EncodedValue.ArrayEncodedSubValue;
import schilling.richard.dexlib.io.AnnotatedOutput;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.io.ReadContext;
import schilling.richard.io.Input;

public class EncodedArrayItem extends Item<EncodedArrayItem> {
	private int hashCode = 0;

	private ArrayEncodedSubValue encodedArray;

	/**
	 * Creates a new uninitialized <code>EncodedArrayItem</code>
	 * 
	 * @param dexFile
	 *            The <code>DexFile</code> that this item belongs to
	 */
	protected EncodedArrayItem(DexFile dexFile) {
		super(dexFile);
	}

	/**
	 * Creates a new <code>EncodedArrayItem</code> with the given values
	 * 
	 * @param dexFile
	 *            The <code>DexFile</code> that this item belongs to
	 * @param encodedArray
	 *            The encoded array value
	 */
	private EncodedArrayItem(DexFile dexFile, ArrayEncodedSubValue encodedArray) {
		super(dexFile);
		this.encodedArray = encodedArray;
	}

	/**
	 * Returns an <code>EncodedArrayItem</code> for the given values, and that
	 * has been interned into the given <code>DexFile</code>
	 * 
	 * @param dexFile
	 *            The <code>DexFile</code> that this item belongs to
	 * @param encodedArray
	 *            The encoded array value
	 * @return an <code>EncodedArrayItem</code> for the given values, and that
	 *         has been interned into the given
	 */
	public static EncodedArrayItem internEncodedArrayItem(DexFile dexFile,
			ArrayEncodedSubValue encodedArray) {
		EncodedArrayItem encodedArrayItem = new EncodedArrayItem(dexFile,
				encodedArray);
		return dexFile.EncodedArraysSection.intern(encodedArrayItem);
	}

	/** {@inheritDoc} */
	@Override
	protected void readItem(Input in, ReadContext readContext) {
		encodedArray = new ArrayEncodedSubValue(dexFile, in);
	}

	/** {@inheritDoc} */
	@Override
	protected int placeItem(int offset) {
		return encodedArray.placeValue(offset);
	}

	/** {@inheritDoc} */
	@Override
	protected void writeItem(AnnotatedOutput out) {
		encodedArray.writeValue(out);
	}

	/** {@inheritDoc} */
	@Override
	public ItemType getItemType() {
		return ItemType.TYPE_ENCODED_ARRAY_ITEM;
	}

	/** {@inheritDoc} */
	@Override
	public String getConciseIdentity() {
		return "encoded_array @0x" + Integer.toHexString(getOffset());
	}

	/** {@inheritDoc} */
	@Override
	public int compareTo(EncodedArrayItem encodedArrayItem) {
		return encodedArray.compareTo(encodedArrayItem.encodedArray);
	}

	/**
	 * @return The encoded array value
	 */
	public ArrayEncodedSubValue getEncodedArray() {
		return encodedArray;
	}

	/**
	 * calculate and cache the hashcode
	 */
	private void calcHashCode() {
		hashCode = encodedArray.hashCode();
	}

	@Override
	public int hashCode() {
		// there's a small possibility that the actual hash code will be 0. If
		// so, we'll
		// just end up recalculating it each time
		if (hashCode == 0)
			calcHashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !this.getClass().equals(o.getClass())) {
			return false;
		}

		EncodedArrayItem other = (EncodedArrayItem) o;
		return (encodedArray.compareTo(other.encodedArray) == 0);
	}

	/**
	 * Clones the values of this class and adds them to a DEX file. All of the
	 * members of the class will be created in the relevant structures. If a
	 * ClassDefItem of the same type already exists, than an exception is
	 * thrown.
	 * 
	 * <p>
	 * This function also makes sure that the references that this class depends
	 * on are also created in the destination file.
	 * 
	 * @param dest
	 *            the destination file.
	 * @return the new ClassDefItem that was created in the destination file.
	 * @throws IllegalArgumentException
	 *             if the destination file already has a class with the same
	 *             signature as this one.
	 * 
	 */
	public EncodedArrayItem cloneInto(DexFile dest) {

		if (dest == null)
			throw new IllegalArgumentException("dex file cannot be null.");

		EncodedArrayItem result = new EncodedArrayItem(dest);

		if (this.encodedArray != null)
			result.encodedArray = (ArrayEncodedSubValue) this.encodedArray
					.cloneInto(dest);

		result.index = this.index;
		result.offset = this.offset;

		EncodedArrayItem matched = dest.EncodedArraysSection.getMatched(result);
		if (matched != null)
			return matched;

		dest.EncodedArraysSection.intern(result);

		return result;

	}
}
