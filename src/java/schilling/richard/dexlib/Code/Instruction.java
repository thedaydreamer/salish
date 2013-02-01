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
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package schilling.richard.dexlib.Code;

import schilling.richard.dexlib.Code.Format.Format;
import schilling.richard.dexlib.io.AnnotatedOutput;
import schilling.richard.dexlib.io.DexFile;

/**
 * An alternate class representation of instructions.
 * 
 * @author rschilling
 * 
 */
public abstract class Instruction {
	private final Opcode opcode;

	public Opcode getOpcode() {
		return opcode;
	}

	/**
	 * Returns the size of this instruction in code blocks, assuming the
	 * instruction is located at the given address
	 * 
	 * @param codeAddress
	 *            the code address where the instruction is located
	 * @return The size of this instruction in code blocks
	 **/
	public int getSize(int codeAddress) {
		return opcode.format.size / 2;
	}

	protected Instruction(Opcode opcode) {
		this.opcode = opcode;
	}

	public abstract Format getFormat();

	public int write(AnnotatedOutput out, int currentCodeAddress) {
		if (out.annotates()) {
			annotateInstruction(out, currentCodeAddress);
		}
		writeInstruction(out, currentCodeAddress);
		return currentCodeAddress + getSize(currentCodeAddress);
	}

	protected void annotateInstruction(AnnotatedOutput out,
			int currentCodeAddress) {
		out.annotate(getSize(currentCodeAddress) * 2,
				"[0x" + Integer.toHexString(currentCodeAddress) + "] "
						+ opcode.name + " instruction");
	}

	protected abstract void writeInstruction(AnnotatedOutput out,
			int currentCodeAddress);

	public static interface InstructionFactory {
		public Instruction makeInstruction(DexFile dexFile, Opcode opcode,
				byte[] buffer, int bufferIndex);
	}

	/**
	 * This method must be implemented by all instructions because of some of
	 * the references that some instructions have.
	 * 
	 * <p>
	 * Requiring this method to be implemented for all instructions supports the
	 * requirement that when an object in the class model is copied, a true and
	 * independent copy of all objects are made.
	 * 
	 * @param dest
	 *            the dex file that this instruction is being cloned into.
	 * @return a clone of this instruction.
	 */
	public abstract Instruction cloneInto(DexFile dest);

}
