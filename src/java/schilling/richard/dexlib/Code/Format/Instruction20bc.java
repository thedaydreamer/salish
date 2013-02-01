/*
 * [The "BSD licence"]
 * Copyright (c) 2011 Ben Gruver
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
import schilling.richard.dexlib.Code.InstructionWithReference;
import schilling.richard.dexlib.Code.Opcode;
import schilling.richard.dexlib.Code.ReferenceType;
import schilling.richard.dexlib.Code.VerificationErrorType;
import schilling.richard.dexlib.io.AnnotatedOutput;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.io.deserialize.FieldIdItem;
import schilling.richard.dexlib.io.deserialize.Item;
import schilling.richard.dexlib.io.deserialize.MethodIdItem;
import schilling.richard.dexlib.io.deserialize.TypeIdItem;
import schilling.richard.dexlib.util.NumberUtils;

public class Instruction20bc extends InstructionWithReference {
	public static final Instruction.InstructionFactory Factory = new Factory();

	private VerificationErrorType validationErrorType;

	public Instruction20bc(Opcode opcode,
			VerificationErrorType validationErrorType, Item referencedItem) {
		super(opcode, referencedItem, getReferenceType(referencedItem));

		this.validationErrorType = validationErrorType;
	}

	private static ReferenceType getReferenceType(Item item) {
		if (item instanceof TypeIdItem) {
			return ReferenceType.type;
		}
		if (item instanceof FieldIdItem) {
			return ReferenceType.field;
		}
		if (item instanceof MethodIdItem) {
			return ReferenceType.method;
		}
		return null;
	}

	private Instruction20bc(DexFile dexFile, Opcode opcode, byte[] buffer,
			int bufferIndex) {
		super(dexFile, opcode, buffer, bufferIndex);

		short val = NumberUtils.decodeUnsignedByte(buffer[bufferIndex + 1]);
		validationErrorType = VerificationErrorType
				.getValidationErrorType(val & 0x3f);
	}

	private Instruction20bc(Opcode opcode) {
		super(opcode);
	}

	@Override
	protected ReferenceType readReferenceType(Opcode opcode, byte[] buffer,
			int bufferIndex) {
		short val = NumberUtils.decodeUnsignedByte(buffer[bufferIndex + 1]);
		short referenceType = (short) (val >> 6);
		return ReferenceType.fromValidationErrorReferenceType(referenceType);
	}

	@Override
	public Format getFormat() {
		return Format.Format20bc;
	}

	@Override
	protected void writeInstruction(AnnotatedOutput out, int currentCodeAddress) {
		if (getOpcode() == Opcode.CONST_STRING
				&& getReferencedItem().getIndex() > 0xFFFF) {
			throw new RuntimeException(
					"String offset is too large for const-string. Use string-const/jumbo instead.");
		}

		out.writeByte(getOpcode().value);
		out.writeByte((this.getReferenceType()
				.getValidationErrorReferenceType() << 6)
				& validationErrorType.getValue());

		out.writeShort(getReferencedItem().getIndex());
	}

	public VerificationErrorType getValidationErrorType() {
		return validationErrorType;
	}

	private static class Factory implements Instruction.InstructionFactory {
		@Override
		public Instruction makeInstruction(DexFile dexFile, Opcode opcode,
				byte[] buffer, int bufferIndex) {
			return new Instruction20bc(dexFile, opcode, buffer, bufferIndex);
		}
	}

	@Override
	public Instruction cloneInto(DexFile dest) {

		if (dest == null)
			throw new IllegalArgumentException("dex file cannot be null.");

		Instruction20bc result = new Instruction20bc(getOpcode());
		result.validationErrorType = this.validationErrorType;

		cloneInto(result, dest);

		return result;

	}
}
