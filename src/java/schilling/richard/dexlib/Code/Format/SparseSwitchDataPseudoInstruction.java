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

import java.util.Iterator;

import schilling.richard.dexlib.Code.Instruction;
import schilling.richard.dexlib.Code.MultiOffsetInstruction;
import schilling.richard.dexlib.Code.Opcode;
import schilling.richard.dexlib.io.AnnotatedOutput;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.util.NumberUtils;

public class SparseSwitchDataPseudoInstruction extends Instruction implements
		MultiOffsetInstruction {
	public static final Instruction.InstructionFactory Factory = new Factory();
	private int[] keys;
	private int[] targets;

	@Override
	public int getSize(int codeAddress) {
		return getTargetCount() * 4 + 2 + (codeAddress % 2);
	}

	public SparseSwitchDataPseudoInstruction(int[] keys, int[] targets) {
		super(Opcode.NOP);

		if (keys.length != targets.length) {
			throw new RuntimeException(
					"The number of keys and targets don't match");
		}

		if (targets.length == 0) {
			throw new RuntimeException(
					"The sparse-switch data must contain at least 1 key/target");
		}

		if (targets.length > 0xFFFF) {
			throw new RuntimeException(
					"The sparse-switch data contains too many elements. "
							+ "The maximum number of switch elements is 65535");
		}

		this.keys = keys;
		this.targets = targets;
	}

	public SparseSwitchDataPseudoInstruction(byte[] buffer, int bufferIndex) {
		super(Opcode.NOP);

		byte opcodeByte = buffer[bufferIndex];
		if (opcodeByte != 0x00) {
			throw new RuntimeException(
					"Invalid opcode byte for a SparseSwitchData pseudo-instruction");
		}
		byte subopcodeByte = buffer[bufferIndex + 1];
		if (subopcodeByte != 0x02) {
			throw new RuntimeException(
					"Invalid sub-opcode byte for a SparseSwitchData pseudo-instruction");
		}

		int targetCount = NumberUtils.decodeUnsignedShort(buffer,
				bufferIndex + 2);
		keys = new int[targetCount];
		targets = new int[targetCount];

		for (int i = 0; i < targetCount; i++) {
			keys[i] = NumberUtils.decodeInt(buffer, bufferIndex + 4 + i * 4);
			targets[i] = NumberUtils.decodeInt(buffer, bufferIndex + 4
					+ targetCount * 4 + i * 4);
		}
	}

	@Override
	protected void writeInstruction(AnnotatedOutput out, int currentCodeAddress) {
		out.alignTo(4);

		out.writeByte(0x00);
		out.writeByte(0x02);
		out.writeShort(targets.length);

		if (targets.length > 0) {
			int key = keys[0];

			out.writeInt(key);

			for (int i = 1; i < keys.length; i++) {
				key = keys[i];
				assert key >= keys[i - 1];
				out.writeInt(key);
			}

			for (int target : targets) {
				out.writeInt(target);
			}
		}
	}

	@Override
	protected void annotateInstruction(AnnotatedOutput out,
			int currentCodeAddress) {
		out.annotate(getSize(currentCodeAddress) * 2,
				"[0x" + Integer.toHexString(currentCodeAddress) + "] "
						+ "sparse-switch-data instruction");
	}

	@Override
	public void updateTarget(int targetIndex, int targetAddressOffset) {
		targets[targetIndex] = targetAddressOffset;
	}

	@Override
	public Format getFormat() {
		return Format.SparseSwitchData;
	}

	public int getTargetCount() {
		return targets.length;
	}

	@Override
	public int[] getTargets() {
		return targets;
	}

	public int[] getKeys() {
		return keys;
	}

	public static class SparseSwitchTarget {
		public int key;
		public int targetAddressOffset;
	}

	public Iterator<SparseSwitchTarget> iterateKeysAndTargets() {
		return new Iterator<SparseSwitchTarget>() {
			final int targetCount = getTargetCount();
			int i = 0;

			SparseSwitchTarget sparseSwitchTarget = new SparseSwitchTarget();

			@Override
			public boolean hasNext() {
				return i < targetCount;
			}

			@Override
			public SparseSwitchTarget next() {
				sparseSwitchTarget.key = keys[i];
				sparseSwitchTarget.targetAddressOffset = targets[i];
				i++;
				return sparseSwitchTarget;
			}

			@Override
			public void remove() {
			}
		};
	}

	private static class Factory implements Instruction.InstructionFactory {
		@Override
		public Instruction makeInstruction(DexFile dexFile, Opcode opcode,
				byte[] buffer, int bufferIndex) {
			if (opcode != Opcode.NOP) {
				throw new RuntimeException(
						"The opcode for a SparseSwitchDataPseudoInstruction must be NOP");
			}
			return new SparseSwitchDataPseudoInstruction(buffer, bufferIndex);
		}
	}

	@Override
	public Instruction cloneInto(DexFile dest) {

		if (dest == null)
			throw new IllegalArgumentException("dex file cannot be null.");

		SparseSwitchDataPseudoInstruction result = new SparseSwitchDataPseudoInstruction(
				getOpcode());

		if (this.targets != null) {
			result.targets = new int[this.targets.length];
			System.arraycopy(this.targets, 0, result.targets, 0,
					result.targets.length);
		}

		if (this.keys != null) {
			result.keys = new int[this.keys.length];
			System.arraycopy(this.keys, 0, result.keys, 0, result.keys.length);
		}

		return result;

	}

	// TODO move this constructor up in the file with the other constructors.
	private SparseSwitchDataPseudoInstruction(Opcode opcode) {
		super(opcode);
	}
}
