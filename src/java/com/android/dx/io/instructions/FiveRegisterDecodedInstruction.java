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

package com.android.dx.io.instructions;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.format.InstructionCodec;

/**
 * A decoded Dalvik instruction which has five register arguments.
 */
public final class FiveRegisterDecodedInstruction extends DecodedInstruction {

	/**
	 * Constructs an instance.
	 */
	public FiveRegisterDecodedInstruction(InstructionCodec format, int opcode,
			int index, IndexType indexType, int target, int relativeTarget,
			long literal, int a, int b, int c, int d, int e) {
		super(format, opcode, index, indexType, target, relativeTarget, literal);

		setA(a);
		setB(b);
		setC(c);
		setD(d);
		setE(e);

	}

	public FiveRegisterDecodedInstruction(InstructionCodec format, int opcode,
			int index, IndexType indexType, int target, int relativeTarget,
			long literal, int a, int b, int c, int d, int e, boolean decRegList) {
		super(format, opcode, index, indexType, target, relativeTarget,
				literal, decRegList);

		setA(a);
		setB(b);
		setC(c);
		setD(d);
		setE(e);

	}

	/** @inheritDoc */
	public int getRegisterCount() {
		return 5;
	}

	/** @inheritDoc */
	public DecodedInstruction withIndex(int newIndex) {
		return new FiveRegisterDecodedInstruction(getFormat(), getOpcode(),
				newIndex, getIndexType(), getTarget(), getRelativeTarget(),
				getLiteral(), getA(), getB(), getC(), getD(), getE(),
				isDecodedFromRegisterList());

	}

}
