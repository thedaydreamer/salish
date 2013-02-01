/*
 * Copyright 2011, Google Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package schilling.richard.dexlib.Code.Format;

import schilling.richard.dexlib.Code.InstructionWithReference;
import schilling.richard.dexlib.Code.Opcode;
import schilling.richard.dexlib.Code.ReferenceType;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.io.deserialize.Item;
import schilling.richard.dexlib.util.NumberUtils;

public abstract class InstructionWithJumboReference extends
		InstructionWithReference {
	protected InstructionWithJumboReference(Opcode opcode, Item referencedItem) {
		super(opcode, referencedItem);
	}

	protected InstructionWithJumboReference(Opcode opcode, Item referencedItem,
			ReferenceType referenceType) {
		super(opcode, referencedItem, referenceType);
	}

	protected InstructionWithJumboReference(DexFile dexFile, Opcode opcode,
			byte[] buffer, int bufferIndex) {
		super(dexFile, opcode, buffer, bufferIndex);
	}

	protected InstructionWithJumboReference(Opcode opcode) {
		super(opcode);
	}

	@Override
	protected int getReferencedItemIndex(byte[] buffer, int bufferIndex) {
		return NumberUtils.decodeInt(buffer, bufferIndex + 2);
	}
}
