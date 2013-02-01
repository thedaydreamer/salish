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

import com.android.dx.io.opcode.format.InstructionCodec;

/**
 * A decoded Dalvik instruction which contains the payload for a
 * {@code packed-switch} instruction.
 */
public final class PackedSwitchPayloadDecodedInstruction extends
		DecodedInstruction {

	/** first key value */
	private final int firstKey;

	/**
	 * array of target addresses. These are absolute, not relative, addresses.
	 */
	private int[] targets;

	/**
	 * Constructs an instance.
	 */
	public PackedSwitchPayloadDecodedInstruction(InstructionCodec format,
			int opcode, int firstKey, int[] targets) {
		super(format, opcode, 0, null, 0, 0, 0L);

		this.firstKey = firstKey;
		this.targets = targets;
	}

	/** @inheritDoc */
	public int getRegisterCount() {
		return 0;
	}

	public int getFirstKey() {
		return firstKey;
	}

	public int[] getTargets() {
		return targets;
	}

	public void setTargets(int[] t) {
		targets = t;
	}
	
	@Override
	/**
	 * Sets the target address.
	 */
	public final void setTarget(int target) {

		// nop
		
	}

	/** @inheritDoc */
	public DecodedInstruction withIndex(int newIndex) {
		throw new UnsupportedOperationException("no index in instruction");
	}

	@Override
	public String toLongString() {
		
		StringBuilder result = new StringBuilder();

		String opcodeString = String.format("%s(%d)@%d ", getOpcodeEnum()
				.toString(), getOpcode(), getAddress());

		result.append(String.format("%-26s ", opcodeString));
		result.append(String.format("size %d ", targets.length));
		result.append(String.format("first key %d ", firstKey));
		
		
		for (int i = 0; i < targets.length; i++){
			result.append(String.format("t%d(%d) ", i, targets[i]));
		}
		
		return result.toString();

	}

}
