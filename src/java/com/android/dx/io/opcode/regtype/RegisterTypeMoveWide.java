package com.android.dx.io.opcode.regtype;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.instructions.DecodedInstruction;

public class RegisterTypeMoveWide extends RegisterTypeSetter {

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		int vSrcLow = instruction.getB();
		int vSrcHigh = vSrcLow + 1;

		int vDstLow = instruction.getA();
		int vDstHigh = vDstLow + 1;

		RegisterType srcTypeLow = registerTypes.get(vSrcLow, instruction);
		RegisterType srcTypeHigh = registerTypes.get(vSrcHigh, instruction);

		registerTypes.put(vDstLow, srcTypeLow, instruction, vSrcLow);

		registerTypes.put(vDstHigh, srcTypeHigh, instruction, vSrcHigh);

	}

}
