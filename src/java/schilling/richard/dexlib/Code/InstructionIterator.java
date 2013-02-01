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

package schilling.richard.dexlib.Code;

import schilling.richard.dexlib.Code.Format.ArrayDataPseudoInstruction;
import schilling.richard.dexlib.Code.Format.Instruction10x;
import schilling.richard.dexlib.Code.Format.PackedSwitchDataPseudoInstruction;
import schilling.richard.dexlib.Code.Format.SparseSwitchDataPseudoInstruction;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.io.util.Hex;

public class InstructionIterator {
	public static void IterateInstructions(DexFile dexFile, byte[] insns,
			ProcessInstructionDelegate delegate) {
		int insnsPosition = 0;

		while (insnsPosition < insns.length) {

			short opcodeValue = (short) (insns[insnsPosition] & 0xFF);
			if (opcodeValue == 0xFF) {
				opcodeValue = (short) ((0xFF << 8) | insns[insnsPosition + 1]);
			}

			Opcode opcode = Opcode.getOpcodeByValue(opcodeValue);

			Instruction instruction = null;

			if (opcode == null) {
				throw new RuntimeException("Unknown opcode: "
						+ Hex.u1(insns[insnsPosition]));
			}

			if (opcode == Opcode.NOP) {
				byte secondByte = insns[insnsPosition + 1];
				switch (secondByte) {
				case 0: {
					instruction = new Instruction10x(Opcode.NOP, insns,
							insnsPosition);
					break;
				}
				case 1: {
					instruction = new PackedSwitchDataPseudoInstruction(insns,
							insnsPosition);
					break;
				}
				case 2: {
					instruction = new SparseSwitchDataPseudoInstruction(insns,
							insnsPosition);
					break;
				}
				case 3: {
					instruction = new ArrayDataPseudoInstruction(insns,
							insnsPosition);
					break;
				}
				}
			} else {
				instruction = opcode.format.Factory.makeInstruction(dexFile,
						opcode, insns, insnsPosition);
			}

			assert instruction != null;

			delegate.ProcessInstruction(insnsPosition / 2, instruction);
			insnsPosition += instruction.getSize(insnsPosition / 2) * 2;

		}
	}

	public static interface ProcessInstructionDelegate {
		public void ProcessInstruction(int codeAddress, Instruction instruction);
	}
}
