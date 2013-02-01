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

package schilling.richard.dexlib.Code.Format;

import schilling.richard.dexlib.Code.Instruction;
import schilling.richard.dexlib.Code.Opcode;
import schilling.richard.dexlib.Code.TwoRegisterInstruction;
import schilling.richard.dexlib.io.AnnotatedOutput;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.util.NumberUtils;

public class Instruction32x extends Instruction implements
		TwoRegisterInstruction {
	public static final Instruction.InstructionFactory Factory = new Factory();
	private short regA;
	private short regB;

	public Instruction32x(Opcode opcode, int regA, int regB) {
		super(opcode);

		if (regA >= 1 << 16 || regB >= 1 << 16) {
			throw new RuntimeException(
					"The register number must be less than v65536");
		}

		this.regA = (short) regA;
		this.regB = (short) regB;
	}

	private Instruction32x(Opcode opcode, byte[] buffer, int bufferIndex) {
		super(opcode);

		this.regA = (short) NumberUtils.decodeUnsignedShort(buffer,
				bufferIndex + 2);
		this.regB = (short) NumberUtils.decodeUnsignedShort(buffer,
				bufferIndex + 4);
	}

	@Override
	protected void writeInstruction(AnnotatedOutput out, int currentCodeAddress) {
		out.writeByte(getOpcode().value);
		out.writeByte(0);
		out.writeShort(regA);
		out.writeShort(regB);
	}

	@Override
	public Format getFormat() {
		return Format.Format32x;
	}

	@Override
	public int getRegisterA() {
		return regA & 0xFFFF;
	}

	@Override
	public int getRegisterB() {
		return regB & 0xFFFF;
	}

	private static class Factory implements Instruction.InstructionFactory {
		@Override
		public Instruction makeInstruction(DexFile dexFile, Opcode opcode,
				byte[] buffer, int bufferIndex) {
			return new Instruction32x(opcode, buffer, bufferIndex);
		}
	}

	@Override
	public Instruction cloneInto(DexFile dest) {

		if (dest == null)
			throw new IllegalArgumentException("dex file cannot be null.");

		Instruction32x result = new Instruction32x(getOpcode());
		result.regA = this.regA;
		result.regB = this.regB;

		return result;

	}

	// TODO move this constructor up in the file with the other constructors.
	private Instruction32x(Opcode opcode) {
		super(opcode);
	}
}