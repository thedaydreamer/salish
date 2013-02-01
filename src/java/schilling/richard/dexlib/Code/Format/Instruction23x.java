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
import schilling.richard.dexlib.Code.ThreeRegisterInstruction;
import schilling.richard.dexlib.io.AnnotatedOutput;
import schilling.richard.dexlib.io.DexFile;

public class Instruction23x extends Instruction implements
		ThreeRegisterInstruction {
	public static final Instruction.InstructionFactory Factory = new Factory();
	private byte regA;
	private byte regB;
	private byte regC;

	public Instruction23x(Opcode opcode, short regA, short regB, short regC) {
		super(opcode);

		if (regA >= 1 << 8 || regB >= 1 << 8 || regC >= 1 << 8) {
			throw new RuntimeException(
					"The register number must be less than v256");
		}

		this.regA = (byte) regA;
		this.regB = (byte) regB;
		this.regC = (byte) regC;
	}

	private Instruction23x(Opcode opcode, byte[] buffer, int bufferIndex) {
		super(opcode);

		this.regA = buffer[bufferIndex + 1];
		this.regB = buffer[bufferIndex + 2];
		this.regC = buffer[bufferIndex + 3];
	}

	@Override
	protected void writeInstruction(AnnotatedOutput out, int currentCodeAddress) {
		out.writeByte(getOpcode().value);
		out.writeByte(regA);
		out.writeByte(regB);
		out.writeByte(regC);
	}

	@Override
	public Format getFormat() {
		return Format.Format23x;
	}

	@Override
	public int getRegisterA() {
		return regA & 0xFF;
	}

	@Override
	public int getRegisterB() {
		return regB & 0xFF;
	}

	@Override
	public int getRegisterC() {
		return regC & 0xFF;
	}

	private static class Factory implements Instruction.InstructionFactory {
		@Override
		public Instruction makeInstruction(DexFile dexFile, Opcode opcode,
				byte[] buffer, int bufferIndex) {
			return new Instruction23x(opcode, buffer, bufferIndex);
		}
	}

	@Override
	public Instruction cloneInto(DexFile dest) {

		if (dest == null)
			throw new IllegalArgumentException("dex file cannot be null.");

		Instruction23x result = new Instruction23x(getOpcode());

		result.regA = this.regA;
		result.regB = this.regB;
		result.regC = this.regC;

		return result;

	}

	// TODO move this constructor up in the file with the other constructors.
	private Instruction23x(Opcode opcode) {
		super(opcode);
	}
}
