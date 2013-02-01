/*
 * [The "BSD licence"]
 * Copyright (c) 2010 Ben Gruver
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

package schilling.richard.dexlib.disassembler.adapter;


import static schilling.richard.r3.app.util.RegisterPreferenceEnum.ALL;
import static schilling.richard.r3.app.util.RegisterPreferenceEnum.ALLPRE;
import static schilling.richard.r3.app.util.RegisterPreferenceEnum.ARGS;
import static schilling.richard.r3.app.util.RegisterPreferenceEnum.FULLMERGE;
import static schilling.richard.r3.app.util.RegisterPreferenceEnum.MERGE;
import java.io.IOException;
import java.util.BitSet;

import schilling.richard.dexlib.Code.FiveRegisterInstruction;
import schilling.richard.dexlib.Code.RegisterRangeInstruction;
import schilling.richard.dexlib.Code.SingleRegisterInstruction;
import schilling.richard.dexlib.Code.ThreeRegisterInstruction;
import schilling.richard.dexlib.Code.TwoRegisterInstruction;
import schilling.richard.dexlib.Code.Analysis.AnalyzedInstruction;
import schilling.richard.dexlib.Code.Analysis.MethodAnalyzer;
import schilling.richard.dexlib.io.deserialize.ClassDataItem;
import schilling.richard.dexlib.io.util.AccessFlags;
import schilling.richard.io.IndentingWriter;
import schilling.richard.r3.app.FinnrApp;

public class PreInstructionRegisterInfoMethodItem extends MethodItem {
	private final AnalyzedInstruction analyzedInstruction;
	private final MethodAnalyzer methodAnalyzer;

	public PreInstructionRegisterInfoMethodItem(
			AnalyzedInstruction analyzedInstruction,
			MethodAnalyzer methodAnalyzer, int codeAddress) {
		super(codeAddress);
		this.analyzedInstruction = analyzedInstruction;
		this.methodAnalyzer = methodAnalyzer;
	}

	@Override
	public double getSortOrder() {
		return 99.9;
	}

	@Override
	public boolean writeTo(IndentingWriter writer) throws IOException {
		int registerInfo = FinnrApp.getApp().getPrefRegisterInfoSetAsBitmap();
		int registerCount = analyzedInstruction.getRegisterCount();
		BitSet registers = new BitSet(registerCount);

		if ((registerInfo & ALL.asInt()) != 0) {
			registers.set(0, registerCount);
		} else {
			if ((registerInfo & ALLPRE.asInt()) != 0) {
				registers.set(0, registerCount);
			} else {
				if ((registerInfo & ARGS.asInt()) != 0) {
					addArgsRegs(registers);
				}
				if ((registerInfo & MERGE.asInt()) != 0) {
					addMergeRegs(registers, registerCount);
				} else if ((registerInfo & FULLMERGE.asInt()) != 0
						&& (analyzedInstruction.isBeginningInstruction())) {
					addParamRegs(registers, registerCount);
				}
			}
		}

		boolean printedSomething = false;
		if ((registerInfo & FULLMERGE.asInt()) != 0) {
			printedSomething = writeFullMergeRegs(writer, registers,
					registerCount);
		}

		printedSomething |= writeRegisterInfo(writer, registers,
				printedSomething);

		return printedSomething;
	}

	private void addArgsRegs(BitSet registers) {
		if (analyzedInstruction.getInstruction() instanceof RegisterRangeInstruction) {
			RegisterRangeInstruction instruction = (RegisterRangeInstruction) analyzedInstruction
					.getInstruction();

			registers.set(instruction.getStartRegister(),
					instruction.getStartRegister() + instruction.getRegCount());
		} else if (analyzedInstruction.getInstruction() instanceof FiveRegisterInstruction) {
			FiveRegisterInstruction instruction = (FiveRegisterInstruction) analyzedInstruction
					.getInstruction();
			int regCount = instruction.getRegCount();
			switch (regCount) {
			case 5:
				registers.set(instruction.getRegisterA());
				// fall through
			case 4:
				registers.set(instruction.getRegisterG());
				// fall through
			case 3:
				registers.set(instruction.getRegisterF());
				// fall through
			case 2:
				registers.set(instruction.getRegisterE());
				// fall through
			case 1:
				registers.set(instruction.getRegisterD());
			}
		} else if (analyzedInstruction.getInstruction() instanceof ThreeRegisterInstruction) {
			ThreeRegisterInstruction instruction = (ThreeRegisterInstruction) analyzedInstruction
					.getInstruction();
			registers.set(instruction.getRegisterA());
			registers.set(instruction.getRegisterB());
			registers.set(instruction.getRegisterC());
		} else if (analyzedInstruction.getInstruction() instanceof TwoRegisterInstruction) {
			TwoRegisterInstruction instruction = (TwoRegisterInstruction) analyzedInstruction
					.getInstruction();
			registers.set(instruction.getRegisterA());
			registers.set(instruction.getRegisterB());
		} else if (analyzedInstruction.getInstruction() instanceof SingleRegisterInstruction) {
			SingleRegisterInstruction instruction = (SingleRegisterInstruction) analyzedInstruction
					.getInstruction();
			registers.set(instruction.getRegisterA());
		}
	}

	private void addMergeRegs(BitSet registers, int registerCount) {
		if (analyzedInstruction.isBeginningInstruction()) {
			addParamRegs(registers, registerCount);
		}

		if (analyzedInstruction.getPredecessorCount() <= 1) {
			// in the common case of an instruction that only has a single
			// predecessor which is the previous
			// instruction, the pre-instruction registers will always match the
			// previous instruction's
			// post-instruction registers
			return;
		}

		// FIXME uncomment
		/*
		 * for (int registerNum = 0; registerNum < registerCount; registerNum++)
		 * { RegisterType mergedRegisterType = analyzedInstruction
		 * .getPreInstructionRegisterType(registerNum);
		 * 
		 * for (AnalyzedInstruction predecessor : analyzedInstruction
		 * .getPredecessors()) { if
		 * (predecessor.getPostInstructionRegisterType(registerNum) !=
		 * mergedRegisterType) { registers.set(registerNum); continue; } } }
		 */
	}

	private void addParamRegs(BitSet registers, int registerCount) {
		ClassDataItem.EncodedMethod encodedMethod = methodAnalyzer.getMethod();
		int parameterRegisterCount = encodedMethod.method.getPrototype()
				.getParameterRegisterCount();
		if ((encodedMethod.accessFlags & AccessFlags.STATIC.getValue()) == 0) {
			parameterRegisterCount++;
		}

		registers.set(registerCount - parameterRegisterCount, registerCount);
	}

	private boolean writeFullMergeRegs(IndentingWriter writer,
			BitSet registers, int registerCount) throws IOException {
		if (analyzedInstruction.getPredecessorCount() <= 1) {
			return false;
		}

		ClassDataItem.EncodedMethod encodedMethod = methodAnalyzer.getMethod();

		boolean firstRegister = true;
		// FIXME uncomment
		/*
		 * for (int registerNum = 0; registerNum < registerCount; registerNum++)
		 * { RegisterType mergedRegisterType = analyzedInstruction
		 * .getPreInstructionRegisterType(registerNum); boolean addRegister =
		 * false;
		 * 
		 * for (AnalyzedInstruction predecessor : analyzedInstruction
		 * .getPredecessors()) { RegisterType predecessorRegisterType =
		 * predecessor .getPostInstructionRegisterType(registerNum); if
		 * (predecessorRegisterType.category != RegisterType.Category.Unknown &&
		 * predecessorRegisterType != mergedRegisterType) {
		 * 
		 * addRegister = true; break; } }
		 * 
		 * if (!addRegister) { continue; }
		 * 
		 * if (firstRegister) { firstRegister = false; } else {
		 * writer.write('\n'); }
		 * 
		 * writer.write('#'); RegisterFormatter.writeTo(writer,
		 * encodedMethod.codeItem, registerNum); writer.write('=');
		 * analyzedInstruction.getPreInstructionRegisterType(registerNum)
		 * .writeTo(writer); writer.write(":merge{");
		 * 
		 * boolean first = true;
		 * 
		 * for (AnalyzedInstruction predecessor : analyzedInstruction
		 * .getPredecessors()) { RegisterType predecessorRegisterType =
		 * predecessor .getPostInstructionRegisterType(registerNum);
		 * 
		 * if (!first) { writer.write(','); }
		 * 
		 * if (predecessor.getInstructionIndex() == -1) { // the fake
		 * "StartOfMethod" instruction writer.write("Start:"); } else {
		 * writer.write("0x"); writer.printUnsignedLongAsHex(methodAnalyzer
		 * .getInstructionAddress(predecessor)); writer.write(':'); }
		 * predecessorRegisterType.writeTo(writer);
		 * 
		 * first = false; } writer.write('}');
		 * 
		 * registers.clear(registerNum); }
		 */
		return !firstRegister;
	}

	private boolean writeRegisterInfo(IndentingWriter writer, BitSet registers,
			boolean addNewline) throws IOException {
		ClassDataItem.EncodedMethod encodedMethod = methodAnalyzer.getMethod();

		int registerNum = registers.nextSetBit(0);
		if (registerNum < 0) {
			return false;
		}

		if (addNewline) {
			writer.write('\n');
		}
		writer.write('#');

		// FIXME uncomment
		/*
		 * for (; registerNum >= 0; registerNum = registers
		 * .nextSetBit(registerNum + 1)) {
		 * 
		 * RegisterType registerType = analyzedInstruction
		 * .getPreInstructionRegisterType(registerNum);
		 * 
		 * RegisterFormatter.writeTo(writer, encodedMethod.codeItem,
		 * registerNum); writer.write('=');
		 * 
		 * if (registerType == null) { writer.write("null"); } else {
		 * registerType.writeTo(writer); } writer.write(';'); }
		 */
		return true;
	}
}
