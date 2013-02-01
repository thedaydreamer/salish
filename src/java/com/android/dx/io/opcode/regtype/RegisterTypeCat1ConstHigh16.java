package com.android.dx.io.opcode.regtype;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.instructions.DecodedInstruction;

public class RegisterTypeCat1ConstHigh16 extends RegisterTypeSetter {

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		/* could be boolean, int, float, or a null reference */
		registerTypes.put(instruction.getA(),
				RegisterType.determineCat1Const(instruction.getB() << 16),
				instruction, null);

	}

}
