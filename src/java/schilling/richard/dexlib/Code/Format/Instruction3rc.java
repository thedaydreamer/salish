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
import schilling.richard.dexlib.Code.InstructionWithReference;
import schilling.richard.dexlib.Code.Opcode;
import schilling.richard.dexlib.Code.RegisterRangeInstruction;
import schilling.richard.dexlib.io.AnnotatedOutput;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.io.deserialize.Item;
import schilling.richard.dexlib.io.deserialize.MethodIdItem;
import schilling.richard.dexlib.io.deserialize.TypeIdItem;
import schilling.richard.dexlib.util.NumberUtils;

public class Instruction3rc extends InstructionWithReference implements
		RegisterRangeInstruction, InstructionWithJumboVariant {
	public static final Instruction.InstructionFactory Factory = new Factory();
	private byte regCount;
	private short startReg;

	public Instruction3rc(Opcode opcode, short regCount, int startReg,
			Item referencedItem) {
		super(opcode, referencedItem);

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

		this.regCount = (byte) regCount;
		this.startReg = (short) startReg;

		checkItem(opcode, referencedItem, regCount);
	}

	private Instruction3rc(DexFile dexFile, Opcode opcode, byte[] buffer,
			int bufferIndex) {
		super(dexFile, opcode, buffer, bufferIndex);

		this.regCount = (byte) NumberUtils
				.decodeUnsignedByte(buffer[bufferIndex + 1]);
		this.startReg = (short) NumberUtils.decodeUnsignedShort(buffer,
				bufferIndex + 4);

		checkItem(opcode, getReferencedItem(), getRegCount());
	}

	@Override
	protected void writeInstruction(AnnotatedOutput out, int currentCodeAddress) {
		if (getReferencedItem().getIndex() > 0xFFFF) {
			if (getOpcode().hasJumboOpcode()) {
				throw new RuntimeException(
						String.format(
								"%s index is too large. Use the %s instruction instead.",
								getOpcode().referenceType.name(), getOpcode()
										.getJumboOpcode().name));
			} else {
				throw new RuntimeException(String.format(
						"%s index is too large.",
						getOpcode().referenceType.name()));
			}
		}

		out.writeByte(getOpcode().value);
		out.writeByte(regCount);
		out.writeShort(this.getReferencedItem().getIndex());
		out.writeShort(startReg);
	}

	@Override
	public Format getFormat() {
		return Format.Format3rc;
	}

	@Override
	public int getRegCount() {
		return (short) (regCount & 0xFF);
	}

	@Override
	public int getStartRegister() {
		return startReg & 0xFFFF;
	}

	private static void checkItem(Opcode opcode, Item item, int regCount) {
		if (opcode == Opcode.FILLED_NEW_ARRAY_RANGE) {
			// check data for filled-new-array/range opcode
			String type = ((TypeIdItem) item).getTypeDescriptor();
			if (type.charAt(0) != '[') {
				throw new RuntimeException("The type must be an array type");
			}
			if (type.charAt(1) == 'J' || type.charAt(1) == 'D') {
				throw new RuntimeException(
						"The type cannot be an array of longs or doubles");
			}
		} else if (opcode.value >= Opcode.INVOKE_VIRTUAL_RANGE.value
				&& opcode.value <= Opcode.INVOKE_INTERFACE_RANGE.value
				|| opcode == Opcode.INVOKE_OBJECT_INIT_RANGE) {
			// check data for invoke-*/range opcodes
			MethodIdItem methodIdItem = (MethodIdItem) item;
			int parameterRegisterCount = methodIdItem.getPrototype()
					.getParameterRegisterCount();
			if (opcode != Opcode.INVOKE_STATIC_RANGE) {
				parameterRegisterCount++;
			}
			if (parameterRegisterCount != regCount) {
				throw new RuntimeException(
						"regCount does not match the number of arguments of the method");
			}
		}
	}

	@Override
	public Instruction makeJumbo() {
		Opcode jumboOpcode = getOpcode().getJumboOpcode();
		if (jumboOpcode == null) {
			return null;
		}

		return new Instruction5rc(jumboOpcode, getRegCount(),
				getStartRegister(), getReferencedItem());
	}

	private static class Factory implements Instruction.InstructionFactory {
		@Override
		public Instruction makeInstruction(DexFile dexFile, Opcode opcode,
				byte[] buffer, int bufferIndex) {
			return new Instruction3rc(dexFile, opcode, buffer, bufferIndex);
		}
	}

	@Override
	public Instruction cloneInto(DexFile dest) {

		if (dest == null)
			throw new IllegalArgumentException("dex file cannot be null.");

		Instruction3rc result = new Instruction3rc(getOpcode());

		result.regCount = this.regCount;
		result.startReg = this.startReg;
		cloneInto(result, dest);

		return result;

	}

	// TODO move this constructor up in the file with the other constructors.
	private Instruction3rc(Opcode opcode) {
		super(opcode);
	}
}
