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
import static schilling.richard.r3.app.util.RegisterPreferenceEnum.ALLPOST;
import static schilling.richard.r3.app.util.RegisterPreferenceEnum.DEST;
import java.io.IOException;
import java.util.BitSet;

import schilling.richard.dexlib.Code.Analysis.AnalyzedInstruction;
import schilling.richard.dexlib.Code.Analysis.MethodAnalyzer;
import schilling.richard.dexlib.io.deserialize.ClassDataItem;
import schilling.richard.io.IndentingWriter;
import schilling.richard.r3.app.FinnrApp;

public class PostInstructionRegisterInfoMethodItem extends MethodItem {
	private final AnalyzedInstruction analyzedInstruction;
	private final MethodAnalyzer methodAnalyzer;

	public PostInstructionRegisterInfoMethodItem(
			AnalyzedInstruction analyzedInstruction,
			MethodAnalyzer methodAnalyzer, int codeAddress) {
		super(codeAddress);
		this.analyzedInstruction = analyzedInstruction;
		this.methodAnalyzer = methodAnalyzer;
	}

	@Override
	public double getSortOrder() {
		return 100.1;
	}

	@Override
	public boolean writeTo(IndentingWriter writer) throws IOException {
		int registerInfo = FinnrApp.getApp().getPrefRegisterInfoSetAsBitmap();
		int registerCount = analyzedInstruction.getRegisterCount();
		BitSet registers = new BitSet(registerCount);

		if ((registerInfo & ALL.asInt()) != 0) {
			registers.set(0, registerCount);
		} else {
			if ((registerInfo & ALLPOST.asInt()) != 0) {
				registers.set(0, registerCount);
			} else if ((registerInfo & DEST.asInt()) != 0) {
				addDestRegs(registers, registerCount);
			}
		}

		return writeRegisterInfo(writer, registers);
	}

	private void addDestRegs(BitSet printPostRegister, int registerCount) {
		for (int registerNum = 0; registerNum < registerCount; registerNum++) {
			if (analyzedInstruction.getPreInstructionRegisterType(registerNum) != analyzedInstruction
					.getPostInstructionRegisterType(registerNum)) {
				printPostRegister.set(registerNum);
			}
		}
	}

	private boolean writeRegisterInfo(IndentingWriter writer, BitSet registers)
			throws IOException {
		ClassDataItem.EncodedMethod encodedMethod = methodAnalyzer.getMethod();

		int registerNum = registers.nextSetBit(0);
		if (registerNum < 0) {
			return false;
		}

		// FIXME uncomment
		/*
		 * writer.write('#'); for (; registerNum >= 0; registerNum = registers
		 * .nextSetBit(registerNum + 1)) {
		 * 
		 * RegisterType registerType = analyzedInstruction
		 * .getPostInstructionRegisterType(registerNum);
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
