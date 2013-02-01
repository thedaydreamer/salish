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

package schilling.richard.dexlib.Code.Format;

import schilling.richard.dexlib.Code.Instruction;
import schilling.richard.dexlib.Code.OdexedInvokeVirtual;
import schilling.richard.dexlib.Code.Opcode;
import schilling.richard.dexlib.Code.RegisterRangeInstruction;
import schilling.richard.dexlib.io.AnnotatedOutput;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.util.NumberUtils;

public class Instruction3rms extends Instruction implements
		RegisterRangeInstruction, OdexedInvokeVirtual {
	public static final Instruction.InstructionFactory Factory = new Factory();
	private byte regCount;
	private short startReg;
	private short vtableIndex;

	public Instruction3rms(Opcode opcode, short regCount, int startReg,
			int vtableIndex) {
		super(opcode);

		if (regCount >= 1 << 8) {
			throw new RuntimeException("regCount must be less than 256");
		}
		if (regCount < 0) {
			throw new RuntimeException("regCount cannot be negative");
		}

		if (startReg >= 1 << 16) {
			throw new RuntimeException(
					"The beginning register of the range must be less than 65536");
		}
		if (startReg < 0) {
			throw new RuntimeException(
					"The beginning register of the range cannot be negative");
		}

		if (vtableIndex >= 1 << 16) {
			throw new RuntimeException(
					"The method index must be less than 65536");
		}

		this.regCount = (byte) regCount;
		this.startReg = (short) startReg;
		this.vtableIndex = (short) vtableIndex;
	}

	private Instruction3rms(Opcode opcode, byte[] buffer, int bufferIndex) {
		super(opcode);

		this.regCount = (byte) NumberUtils
				.decodeUnsignedByte(buffer[bufferIndex + 1]);
		this.vtableIndex = (short) NumberUtils.decodeUnsignedShort(buffer,
				bufferIndex + 2);
		this.startReg = (short) NumberUtils.decodeUnsignedShort(buffer,
				bufferIndex + 4);
	}

	@Override
	protected void writeInstruction(AnnotatedOutput out, int currentCodeAddress) {
		out.writeByte(getOpcode().value);
		out.writeByte(regCount);
		out.writeShort(vtableIndex);
		out.writeShort(startReg);
	}

	@Override
	public Format getFormat() {
		return Format.Format3rms;
	}

	@Override
	public int getRegCount() {
		return (short) (regCount & 0xFF);
	}

	@Override
	public int getStartRegister() {
		return startReg & 0xFFFF;
	}

	@Override
	public int getVtableIndex() {
		return vtableIndex & 0xFFFF;
	}

	private static class Factory implements Instruction.InstructionFactory {
		@Override
		public Instruction makeInstruction(DexFile dexFile, Opcode opcode,
				byte[] buffer, int bufferIndex) {
			return new Instruction3rms(opcode, buffer, bufferIndex);
		}
	}

	@Override
	public Instruction cloneInto(DexFile dest) {

		if (dest == null)
			throw new IllegalArgumentException("dex file cannot be null.");

		Instruction3rms result = new Instruction3rms(getOpcode());

		result.regCount = this.regCount;
		result.startReg = this.startReg;
		result.vtableIndex = this.vtableIndex;

		return result;

	}

	// TODO move this constructor up in the file with the other constructors.
	private Instruction3rms(Opcode opcode) {
		super(opcode);
	}
}
