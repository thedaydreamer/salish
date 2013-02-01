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
import schilling.richard.dexlib.Code.LiteralInstruction;
import schilling.richard.dexlib.Code.Opcode;
import schilling.richard.dexlib.Code.TwoRegisterInstruction;
import schilling.richard.dexlib.io.AnnotatedOutput;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.util.NumberUtils;

public class Instruction22s extends Instruction implements
		TwoRegisterInstruction, LiteralInstruction {
	public static final Instruction.InstructionFactory Factory = new Factory();
	private byte regA;
	private byte regB;
	private short litC;

	public Instruction22s(Opcode opcode, byte regA, byte regB, short litC) {
		super(opcode);

		if (regA >= 1 << 4 || regB >= 1 << 4) {
			throw new RuntimeException(
					"The register number must be less than v16");
		}

		this.regA = regA;
		this.regB = regB;
		this.litC = litC;
	}

	private Instruction22s(Opcode opcode, byte[] buffer, int bufferIndex) {
		super(opcode);

		this.regA = NumberUtils
				.decodeLowUnsignedNibble(buffer[bufferIndex + 1]);
		this.regB = NumberUtils
				.decodeHighUnsignedNibble(buffer[bufferIndex + 1]);
		this.litC = NumberUtils.decodeShort(buffer, bufferIndex + 2);
	}

	@Override
	protected void writeInstruction(AnnotatedOutput out, int currentCodeAddress) {
		out.writeByte(getOpcode().value);
		out.writeByte((regB << 4) | regA);
		out.writeShort(litC);
	}

	@Override
	public Format getFormat() {
		return Format.Format22s;
	}

	@Override
	public int getRegisterA() {
		return regA;
	}

	@Override
	public int getRegisterB() {
		return regB;
	}

	@Override
	public long getLiteral() {
		return litC;
	}

	private static class Factory implements Instruction.InstructionFactory {
		@Override
		public Instruction makeInstruction(DexFile dexFile, Opcode opcode,
				byte[] buffer, int bufferIndex) {
			return new Instruction22s(opcode, buffer, bufferIndex);
		}
	}

	@Override
	public Instruction cloneInto(DexFile dest) {

		if (dest == null)
			throw new IllegalArgumentException("dex file cannot be null.");

		Instruction22s result = new Instruction22s(getOpcode());
		result.litC = this.litC;
		result.regA = this.regA;
		result.regB = this.regB;

		return result;

	}

	// TODO move this constructor up in the file with the other constructors.
	private Instruction22s(Opcode opcode) {
		super(opcode);
	}
}
