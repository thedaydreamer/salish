/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dx.io;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import android.util.SparseIntArray;

import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.opcode.RegisterMap;
import com.android.dx.merge.MethodDefinition;

public class Code implements Cloneable {

	private static final String LOG_TAG = "Code";

	/**
	 * The number of registers used by the function.
	 */
	private int registersSize;

	/**
	 * The number of parameter registers, including this if applicable.
	 */
	private int insSize;
	private int outsSize;
	private int debugInfoOffset;
	private short[] instructions;
	private Try[] tries;
	private CatchHandler[] catchHandlers;

	/**
	 * Offset into the buffer that this code item starts.
	 */
	private int offset;

	/**
	 * The number of bytes that this code item takes up in the buffer.
	 */
	private int size;

	/**
	 * The instructions as a sparse array. Do not write to file.
	 */
	private DecodedInstruction[] decoded;

	/**
	 * @param registersSize
	 *            number of registers
	 * @param insSize
	 * @param outsSize
	 * @param debugInfoOffset
	 * @param instructions
	 * @param tries
	 * @param catchHandlers
	 * @param size
	 *            the number of bytes taken up by the code section.
	 */
	public Code(int registersSize, int insSize, int outsSize,
			int debugInfoOffset, short[] instructions, Try[] tries,
			CatchHandler[] catchHandlers, int offset, int size) {
		this.registersSize = registersSize;
		this.insSize = insSize;
		this.outsSize = outsSize;
		this.debugInfoOffset = debugInfoOffset;
		this.instructions = instructions;
		this.tries = tries;
		this.catchHandlers = catchHandlers;
		this.offset = offset;
		this.size = size;

		if (getFirstArgRegister() < 0)
			throw new IllegalStateException(
					"A code object contains an invalid register number for the first argument");

	}

	public int getOffset() {
		return offset;
	}

	/**
	 * Returns the amount of space this code object takes up in the data file.
	 * 
	 * @return
	 */
	public int getSizeOf() {
		return size;
	}

	public int getRegistersSize() {
		return registersSize;
	}

	public void setRegistersSize(int registersSize) {
		this.registersSize = registersSize;
	}

	/**
	 * Return the number of registers used as parameters. Long and double
	 * parameters occupy two registers. Includes a register for the this pointer
	 * if this is a non static method.
	 * 
	 * @return registers used as parameters.
	 */
	public int getInsSize() {
		return insSize;
	}

	public int getOutsSize() {
		return outsSize;
	}

	public int getDebugInfoOffset() {
		return debugInfoOffset;
	}

	public short[] getInstructions() {
		return instructions;
	}

	/**
	 * Returns a new instance of InstructionList that contains the decoded
	 * instructions for this code object.
	 * 
	 * @return a new instance of InstuctionList filled with instructions.
	 */
	public InstructionList decodeAllAsList() {

		return InstructionList.allocate(decodeAll());

	}

	/**
	 * Decodes all of the instructions in this code object and sets the
	 * singleton for decoded instructions. calling clearDecodedSingleton will
	 * clear the singleton. Each instruction is set with extra data found in
	 * mDef.
	 * 
	 * @param mDef
	 *            MethodDefinition object to get extra data for each
	 *            instruction.
	 * @return an instruction sparse array.
	 */
	public InstructionList decodeAllAsList(MethodDefinition mDef) {
		return InstructionList.allocate(decodeAll(mDef));
	}

	public boolean hasInstructionAtDecodedPosition(int position) {
		return (decodeAllAsList().size() > position);
	}

	public RegisterMap getRegisterMap() {
		return new RegisterMap(decodeAllAsList());
	}

	/**
	 * Decodes the instructions returned from getInstructions(). Some array
	 * elements will be null, but the array will have the same length as the
	 * array returned from getInstructions(). Modifying any DecodedInstruciton
	 * in the resulting list will not affect the underlying short array.
	 * 
	 * @return
	 */
	public DecodedInstruction[] decodeAll() {

		if (decoded == null)
			decoded = DecodedInstruction.decodeAll(getInstructions());

		return decoded;

	}

	/**
	 * Decodes all the instructions in this code object and sets each
	 * instruction with extra data taken from mDef. The position of the array is
	 * the address of the instruction and matches the address value in each
	 * instruction as well. Some cells will have null values and the data from
	 * those cells are rolled up into the DecodedInstruction that appears before
	 * each respective null cell.
	 * 
	 * @param mDef
	 *            the MethodDefinition to retrieve extra information from.
	 * @return an array that contains this code object's instructions decoded.
	 */
	public DecodedInstruction[] decodeAll(MethodDefinition mDef) {
		if (decoded == null)
			decoded = DecodedInstruction.decodeAll(mDef, getInstructions());

		return decoded;
	}

	/**
	 * Clears the decoded singleton so it's created next time a decodeAll
	 * function is called.
	 */
	public void clearDecodedSingleton() {
		decoded = null;
	}

	public Try[] getTries() {
		return tries;
	}

	public CatchHandler[] getCatchHandlers() {
		return catchHandlers;
	}

	/**
	 * Returns the register number that is the first parameter. If this is code
	 * object for a non static function then the value returned by this function
	 * is the register number associated with the 'this' pointer.
	 * 
	 * @return the register number of the first parameter passed into the
	 *         function.
	 */
	public int getFirstArgRegister() {
		return getRegistersSize() - getInsSize();
	}

	/**
	 * Generates a register type sparse array for all the registers referenced
	 * in this format. The integer is the register number referenced. And the
	 * value is the register type. The register category can also be obtained
	 * from RegisterType.
	 * 
	 * <p>
	 * For an example of how register types are set, look at the function
	 * determineCat1Const in the file CodeVerify.cpp
	 * 
	 * @return the register types used by this instruction.
	 */
	public RegisterTypeSparseArray getRegisterTypes() {

		// set instructions from the signature.

		// iterate the instruction set.

		// merge all the instruction sets

		// throw an exception if any register types are not compatible
		// (verification).
		return null;
	}

	public static class Try implements Cloneable {

		/**
		 * The start address of hte try block.
		 */
		private int startAddress;

		/**
		 * The number of instructions in the try block.
		 */
		private int instructionCount;

		/**
		 * The number of byte offset into the dexfile's handler
		 * (encoded_catch_handler_list) list that the handlers for this try
		 * block is found.
		 */
		private int handlerOffset;

		public Try(int startAddress, int instructionCount, int handlerOffset) {
			this.startAddress = startAddress;
			this.instructionCount = instructionCount;
			this.handlerOffset = handlerOffset;
		}

		public int getStartAddress() {
			return startAddress;
		}

		public int getInstructionCount() {
			return instructionCount;
		}

		public int getHandlerOffset() {
			return handlerOffset;
		}

		@Override
		public Object clone() {
			return new Try(startAddress, instructionCount, handlerOffset);
		}

		public void setStartAddress(int newAddress) {
			startAddress = newAddress;

		}
	}

	public static class CatchHandler implements Cloneable {

		/**
		 * The list of type indexes that indicate what exception types the try
		 * block catches.
		 */
		final int[] typeIndexes;

		/**
		 * For each item in typeIndexes, this is the offset into the code where
		 * the catch instructions associated with the corresponding exception
		 * type are found. The address is zero based from the start of the
		 * address block.
		 */
		final int[] addresses;

		/**
		 * This is the address of the catch all block.
		 */
		int catchAllAddress;

		/**
		 * This a convenience array that stores the signatures of the types
		 * found in typeAddresses. Kept in this class for handy reference.
		 */
		String[] typeSignatures;

		/**
		 * Offset in bytes from the start of the associated
		 * encoded_catch_hander_list that this catch handler was found. This
		 * matches the value Try.handlerOffset. This allows us to locate a Try
		 * object's CatchHandler object after it has been read from the buffer.
		 */
		int handlerOffset;

		@Override
		public Object clone() {

			int[] newTypeIndexes = new int[typeIndexes.length];
			System.arraycopy(typeIndexes, 0, newTypeIndexes, 0,
					newTypeIndexes.length);

			int[] newAddresses = new int[addresses.length];
			System.arraycopy(newAddresses, 0, newAddresses, 0,
					newAddresses.length);

			String[] newTypeSignatures = new String[typeSignatures.length];
			System.arraycopy(typeSignatures, 0, newTypeSignatures, 0,
					newTypeSignatures.length);

			return new CatchHandler(newTypeIndexes, newTypeSignatures,
					newAddresses, catchAllAddress);

		}

		public CatchHandler(int[] typeIndexes, String[] typeSignatures,
				int[] addresses, int catchAllAddress) {
			this.typeIndexes = typeIndexes;
			this.addresses = addresses;
			this.catchAllAddress = catchAllAddress;
			this.typeSignatures = typeSignatures;
		}

		public int getHandlerOffset() {
			return handlerOffset;
		}

		public int[] getTypeIndexes() {
			return typeIndexes;
		}

		public String[] getTypeSignatures() {
			return typeSignatures;
		}

		public int[] getAddresses() {
			return addresses;
		}

		public int getCatchAllAddress() {
			return catchAllAddress;
		}

		/**
		 * Sets all of the catch addresses to the new address if the address
		 * matches oldAddress.
		 * 
		 * @param oldAddress
		 *            the address to check for.
		 * @param newAddress
		 *            the address to update to.
		 */
		public void setAddresses(SparseIntArray addressMap) {

			for (int i = 0; i < addresses.length; i++) {

				if (addressMap.indexOfKey(addresses[i]) >= 0) {
					int newAddress = addressMap.get(addresses[i]);
					addresses[i] = newAddress;
				}

			}

			if (addressMap.indexOfKey(catchAllAddress) >= 0) {
				int newAddress = addressMap.get(catchAllAddress);
				catchAllAddress = newAddress;
			}

		}

	}

	@Override
	public Object clone() {
		short[] newInsn = new short[instructions.length];
		System.arraycopy(instructions, 0, newInsn, 0, newInsn.length);

		Try[] newTries = new Try[tries.length];
		for (int i = 0; i < tries.length; i++) {
			newTries[i] = (Try) tries[i].clone();
		}

		CatchHandler[] newHandlers = new CatchHandler[catchHandlers.length];
		for (int i = 0; i < catchHandlers.length; i++) {
			newHandlers[i] = (CatchHandler) catchHandlers[i].clone();
		}

		return new Code(registersSize, insSize, outsSize, debugInfoOffset,
				newInsn, newTries, newHandlers, offset, size);

	}

	/**
	 * Sets the values of this object to the specfied code object. This is done
	 * as a straight copy so the arrays catchHandlers, instructions, and tries
	 * will point to the same respective objects at the end of the operation.
	 * 
	 * @param code
	 *            the code to copy.
	 */
	public void resetTo(Code code) {
		this.catchHandlers = code.catchHandlers;
		this.debugInfoOffset = code.debugInfoOffset;
		this.decoded = null;
		this.insSize = code.insSize;
		this.instructions = code.instructions;
		this.offset = code.offset;
		this.outsSize = code.outsSize;
		this.registersSize = code.registersSize;
		this.size = code.size;
		this.tries = code.tries;

	}

	public void setInstructions(short[] newInstructions) {
		instructions = newInstructions;
		decoded = null;
	}

	/**
	 * sets the outs register size for this code object.
	 * 
	 * @param outsSize
	 * @throws IllegalArgumentException
	 *             if outsSize is larger than registersSize.
	 */
	public void setOutsSize(int outsSize) {
		if (outsSize > registersSize)
			throw new IllegalArgumentException(
					String.format(
							"outsSize(%d) cannot be larger than registersSize(%d).  Update registerSize first.",
							outsSize, registersSize));

		this.outsSize = outsSize;
	}
}
